class Recipe {
  final String id;
  final String title;
  final String? imageUrl;
  String? category;
  final String? area;
  final String? instructions;
  final String? youtubeUrl;
  final List<Ingredient>? ingredients;
  bool isFavorite;

  Recipe({
    required this.id,
    required this.title,
    this.imageUrl,
    this.category,
    this.area,
    this.instructions,
    this.youtubeUrl,
    this.ingredients,
    this.isFavorite = false,
  });

  factory Recipe.fromJson(Map<String, dynamic> json) {
    List<Ingredient>? ingredients;
    if (json.containsKey('strIngredient1')) {
      ingredients = [];
      for (int i = 1; i <= 20; i++) {
        final ingredient = json['strIngredient$i'];
        final measure = json['strMeasure$i'];
        if (ingredient != null && ingredient.toString().trim().isNotEmpty) {
          ingredients.add(Ingredient(
            name: ingredient.toString(),
            measure: measure?.toString() ?? '',
          ));
        }
      }
    }

    return Recipe(
      id: json['idMeal']?.toString() ?? json['id']?.toString() ?? '',
      title: json['strMeal'] ?? json['title'] ?? '',
      imageUrl: json['strMealThumb'] ?? json['imageUrl'],
      category: json['strCategory'],
      area: json['strArea'],
      instructions: json['strInstructions'] ?? json['instructions'],
      youtubeUrl: json['strYoutube'],
      ingredients: ingredients,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'imageUrl': imageUrl,
      'category': category,
      'area': area,
      'instructions': instructions,
      'youtubeUrl': youtubeUrl,
    };
  }
}

class Ingredient {
  final String name;
  final String measure;
  Ingredient({required this.name, required this.measure});
}