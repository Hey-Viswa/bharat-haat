# Sample Data Seeding for Bharat Haat

This document explains how to populate your Firestore database with sample product data for testing the Product Catalog feature.

## Overview

The project includes a data seeding utility that will add 8 sample products to your Firestore `products` collection. This is useful for testing the ProductListScreen and ProductDetailScreen functionality.

## Sample Products Included

The seeded data includes diverse products representing authentic Indian marketplace items:

1. **Handwoven Banarasi Silk Saree** - Traditional Clothing
2. **Brass Kitchen Utensils Set** - Kitchen & Cookware  
3. **Handcarved Sandalwood Ganesha** - Handicrafts & Art
4. **Organic Spice Collection** - Organic Food
5. **Block Print Cotton Kurta** - Traditional Clothing
6. **Handmade Terracotta Pottery Set** - Home Decor
7. **Oxidized Silver Jhumka Earrings** - Jewelry & Accessories
8. **Handloom Cotton Bed Sheet Set** - Textiles & Fabrics

Each product includes:
- Complete product information (name, description, price, category)
- Multiple image URLs from Unsplash
- Seller information
- Stock levels, ratings, and specifications
- All fields required by the Product data model

## How to Seed Data

### Method 1: Using SeedDataActivity (Recommended)

1. **Launch the Seeding Activity**: Use the `DebugUtils` class in your code:
   ```kotlin
   import com.optivus.bharathaat.utils.DebugUtils
   
   // Launch the seeding activity from any context
   DebugUtils.launchSeedDataActivity(context)
   ```

2. **Manual Launch**: You can also start the activity directly:
   ```kotlin
   val intent = Intent(context, SeedDataActivity::class.java)
   context.startActivity(intent)
   ```

3. **Tap the Button**: In the SeedDataActivity, tap "Seed Products Data" to populate Firestore
4. **Wait for Completion**: A toast message will confirm when seeding is complete
5. **Activity Closes**: The activity automatically closes after successful seeding

### Method 2: Programmatic Seeding

You can also call the seeding function directly from your code:

```kotlin
class YourActivity : ComponentActivity() {
    @Inject
    lateinit var dataSeeder: DataSeeder
    
    private fun seedData() {
        lifecycleScope.launch {
            val result = dataSeeder.seedProductData()
            result.fold(
                onSuccess = { message -> 
                    // Handle success
                    Log.d("DataSeeding", message)
                },
                onFailure = { exception -> 
                    // Handle error
                    Log.e("DataSeeding", "Failed: ${exception.message}")
                }
            )
        }
    }
}
```

## Prerequisites

1. **Firebase Configuration**: Ensure your Firebase project is properly configured
2. **Firestore Setup**: Firestore database must be initialized  
3. **Internet Connection**: Required for uploading data to Firestore
4. **Permissions**: App needs internet permissions (already included in manifest)

## Firestore Collection Structure

The data will be added to the `products` collection with document IDs:
- `prod_001` through `prod_008`

Each document follows the `Product` data model defined in your project.

## Testing the Product Catalog

After seeding the data, you can test:

1. **ProductListScreen**: Navigate to the product list to see all 8 products
2. **ProductDetailScreen**: Tap any product to view detailed information
3. **Search/Filter**: Test search and filtering functionality with the seeded data
4. **Categories**: Products are distributed across different categories

## Image URLs

All product images use Unsplash URLs for high-quality, relevant product photos. The images are:
- Free to use
- High resolution (500px width)
- Relevant to the product categories
- Properly formatted for your image loading libraries

## Troubleshooting

**Seeding Fails**:
- Check Firebase connection
- Verify Firestore rules allow writes
- Ensure internet connectivity

**Duplicate Data**:
- The seeding will overwrite existing products with the same IDs
- Safe to run multiple times

**Image Loading Issues**:
- Verify your image loading library (Coil/Glide) is properly configured
- Check internet permissions

## Production Considerations

- This seeding utility is intended for development/testing only
- Remove or disable seeding functionality before production release
- Consider adding user confirmation dialogs for production environments
- The `SeedDataActivity` is not exported in the manifest for security

## Next Steps

After seeding the data:
1. Test the ProductListScreen UI
2. Verify ProductDetailScreen navigation
3. Test search and filtering features
4. Implement additional product management features
5. Add real product data through admin interfaces
