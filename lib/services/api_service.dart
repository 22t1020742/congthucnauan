import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user.dart';
import '../models/recipe.dart';
import '../models/auth_response.dart';

class ApiService{
  //Android: http://192.168.1.7:8080/api
  //Web: http://localhost:8080/api
  static const String baseUrl = 'http://172.20.10.3:8080/api';
  
  Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }

  Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('token', token);
  }

  Future<void> removeToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
  }

  Map<String, String> getHeaders({bool needsAuth = false}) {
    final headers = {'Content-Type': 'application/json'};
    return headers;
  }

  Future<Map<String, String>> getAuthHeaders() async {
    final token = await getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }

  Future<AuthResponse> register(String username, String email, String password, String fullName) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/register'),
      headers: getHeaders(),
      body: jsonEncode({
        'username': username,
        'email': email,
        'password': password,
        'fullName': fullName,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      await saveToken(data['token']);
      return AuthResponse.fromJson(data);
    } else {
      throw Exception(jsonDecode(response.body)['message'] ?? 'Đăng ký thất bại');
    }
  }

  Future<AuthResponse> login(String username, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login'),
      headers: getHeaders(),
      body: jsonEncode({
        'username': username,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      await saveToken(data['token']);
      return AuthResponse.fromJson(data);
    } else {
      throw Exception(jsonDecode(response.body)['message'] ?? 'Đăng nhập thất bại');
    }
  }

  Future<void> logout() async {
    await removeToken();
  }

  Future<List<Recipe>> searchRecipes(String query) async {
    final response = await http.get(
      Uri.parse('$baseUrl/recipes/search?query=$query'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['meals'] == null) return [];
      final results = data['meals'] as List;
      return results.map((json) => Recipe.fromJson(json)).toList();
    } else {
      throw Exception('Tìm kiếm thất bại');
    }
  }

  Future<List<Recipe>> getRandomRecipes({int count = 10}) async {
    List<Recipe> recipes = [];
    for (int i = 0; i < count; i++) {
      try {
        final response = await http.get(
          Uri.parse('$baseUrl/recipes/random'),
          headers: await getAuthHeaders(),
        );
        if (response.statusCode == 200) {
          final data = jsonDecode(response.body);
          if (data['meals'] != null && data['meals'].isNotEmpty) {
            recipes.add(Recipe.fromJson(data['meals'][0]));
          }
        }
      } catch (e) {
        print('Error fetching random recipe: $e');
      }
    }
    return recipes;
  }

  Future<Recipe> getRecipeDetail(String id) async {
    final response = await http.get(
      Uri.parse('$baseUrl/recipes/$id'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['meals'] != null && data['meals'].isNotEmpty) {
        return Recipe.fromJson(data['meals'][0]);
      }
      throw Exception('Không tìm thấy công thức');
    } else {
      throw Exception('Lấy chi tiết thất bại');
    }
  }

  Future<List<String>> getCategories() async {
    final response = await http.get(
      Uri.parse('$baseUrl/recipes/categories'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['categories'] != null) {
        final categories = data['categories'] as List;
        return categories.map((c) => c['strCategory'].toString()).toList();
      }
      return [];
    } else {
      throw Exception('Lấy danh sách categories thất bại');
    }
  }

  Future<List<Recipe>> getRecipesByCategory(String category) async {
    final response = await http.get(
      Uri.parse('$baseUrl/recipes/category?c=$category'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      if (data['meals'] == null) return [];
      final results = data['meals'] as List;
      return results.map((json) => Recipe.fromJson(json)).toList();
    } else {
      throw Exception('Lấy món ăn theo category thất bại');
    }
  }

  Future<void> addToFavorites(Recipe recipe) async {
    final headers = await getAuthHeaders();
    headers['Content-Type'] = 'application/json';
    
    final response = await http.post(
      Uri.parse('$baseUrl/favorites'),
      headers: headers,
      body: jsonEncode({
        'recipeId': recipe.id,
      }),
    );

    if (response.statusCode != 200 && response.statusCode != 201) {
      try {
        final error = jsonDecode(response.body);
        throw Exception(error['message'] ?? 'Thêm yêu thích thất bại');
      } catch (e) {
        throw Exception('Thêm yêu thích thất bại: ${response.body}');
      }
    }
  }

  Future<void> removeFromFavorites(String recipeId) async {
    final response = await http.delete(
      Uri.parse('$baseUrl/favorites/$recipeId'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode != 200) {
      final error = jsonDecode(response.body);
      throw Exception(error['message'] ?? 'Xóa yêu thích thất bại');
    }
  }

  Future<List<Recipe>> getFavorites() async {
    final response = await http.get(
      Uri.parse('$baseUrl/favorites'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final results = jsonDecode(response.body) as List;
      List<Recipe> recipes = [];
      
      for (var favorite in results) {
        try {
          final recipeId = favorite['recipeId'];
          final recipe = await getRecipeDetail(recipeId);
          recipes.add(recipe);
        } catch (e) {
          print('Error loading favorite recipe: $e');
        }
      }
      
      return recipes;
    } else {
      throw Exception('Lấy danh sách yêu thích thất bại');
    }
  }

  Future<bool> checkFavorite(String recipeId) async {
    final response = await http.get(
      Uri.parse('$baseUrl/favorites/check/$recipeId'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return data['message'] == 'true';
    }
    return false;
  }

  Future<User> getUserProfile() async {
    final response = await http.get(
      Uri.parse('$baseUrl/auth/profile'),
      headers: await getAuthHeaders(),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return User.fromJson(data);
    } else {
      throw Exception('Lấy thông tin thất bại');
    }
  }

  Future<User> updateProfile({String? fullName, String? email}) async {
    final response = await http.put(
      Uri.parse('$baseUrl/auth/profile'),
      headers: await getAuthHeaders(),
      body: jsonEncode({
        if (fullName != null) 'fullName': fullName,
        if (email != null) 'email': email,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return User.fromJson(data);
    } else {
      final error = jsonDecode(response.body);
      throw Exception(error['message'] ?? 'Cập nhật thất bại');
    }
  }
}