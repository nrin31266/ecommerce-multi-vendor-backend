package com.vanrin05.app.data;

import com.vanrin05.app.model.Category;

import java.util.List;

public class CategoriesData {
    public static final List<Category> MAIN_CATEGORIES = List.of(
            new Category("Home & Furniture", "home_furniture", 1, null),
            new Category("Men", "men", 1, null),
            new Category("Women", "women", 1, null),
            new Category("Electronics", "electronics", 1, null)
    );

    public static final List<Category> MEN_LEVEL_TWO = List.of(
            new Category("Tops", "men_tops", 2, "men"),
            new Category("Bottoms", "men_bottoms", 2, "men"),
            new Category("Footwear", "men_footwear", 2, "men"),
            new Category("Accessories", "men_accessories", 2, "men"),
            new Category("Outerwear", "men_outerwear", 2, "men"),
            new Category("Sportswear", "men_sportswear", 2, "men")
    );

    public static final List<Category> WOMEN_LEVEL_TWO = List.of(
            new Category("Tops", "women_tops", 2, "women"),
            new Category("Bottoms", "women_bottoms", 2, "women"),
            new Category("Footwear", "women_footwear", 2, "women"),
            new Category("Accessories", "women_accessories", 2, "women"),
            new Category("Dresses", "women_dresses", 2, "women"),
            new Category("Sportswear", "women_sportswear", 2, "women")
    );
    public static final List<Category> HOME_FURNITURE_LEVEL_TWO = List.of(
            new Category("Living Room", "home_living_room", 2, "home_furniture"),
            new Category("Bedroom", "home_bedroom", 2, "home_furniture"),
            new Category("Kitchen & Dining", "home_kitchen_dining", 2, "home_furniture"),
            new Category("Storage & Organization", "home_storage_organization", 2, "home_furniture"),
            new Category("Home Decor", "home_decor", 2, "home_furniture"),
            new Category("Outdoor & Garden", "home_outdoor_garden", 2, "home_furniture")
    );
    public static final List<Category> ELECTRONICS_LEVEL_TWO = List.of(
            new Category("Mobile Phones", "electronics_mobile_phones", 2, "electronics"),
            new Category("Laptops & Computers", "electronics_laptops_computers", 2, "electronics"),
            new Category("TVs & Home Entertainment", "electronics_tvs_home_entertainment", 2, "electronics"),
            new Category("Cameras & Accessories", "electronics_cameras_accessories", 2, "electronics"),
            new Category("Audio Devices", "electronics_audio_devices", 2, "electronics"),
            new Category("Smart Home Devices", "electronics_smart_home_devices", 2, "electronics")
    );
    public static final List<Category> MEN_LEVEL_THREE = List.of(
            // Tops
            new Category("T-Shirts", "men_tops_tshirts", 3, "men_tops"),
            new Category("Shirts", "men_tops_shirts", 3, "men_tops"),
            new Category("Sweaters", "men_tops_sweaters", 3, "men_tops"),
            new Category("Hoodies", "men_tops_hoodies", 3, "men_tops"),
            new Category("Tank Tops", "men_tops_tanktops", 3, "men_tops"),
            new Category("Polo Shirts", "men_tops_polo", 3, "men_tops"),

            // Bottoms
            new Category("Jeans", "men_bottoms_jeans", 3, "men_bottoms"),
            new Category("Trousers", "men_bottoms_trousers", 3, "men_bottoms"),
            new Category("Shorts", "men_bottoms_shorts", 3, "men_bottoms"),
            new Category("Joggers", "men_bottoms_joggers", 3, "men_bottoms"),
            new Category("Cargo Pants", "men_bottoms_cargo", 3, "men_bottoms"),
            new Category("Chinos", "men_bottoms_chinos", 3, "men_bottoms"),

            // Footwear
            new Category("Sneakers", "men_footwear_sneakers", 3, "men_footwear"),
            new Category("Formal Shoes", "men_footwear_formal", 3, "men_footwear"),
            new Category("Boots", "men_footwear_boots", 3, "men_footwear"),
            new Category("Loafers", "men_footwear_loafers", 3, "men_footwear"),
            new Category("Sandals", "men_footwear_sandals", 3, "men_footwear"),
            new Category("Slippers", "men_footwear_slippers", 3, "men_footwear"),

            // Accessories
            new Category("Watches", "men_accessories_watches", 3, "men_accessories"),
            new Category("Belts", "men_accessories_belts", 3, "men_accessories"),
            new Category("Sunglasses", "men_accessories_sunglasses", 3, "men_accessories"),
            new Category("Hats", "men_accessories_hats", 3, "men_accessories"),
            new Category("Gloves", "men_accessories_gloves", 3, "men_accessories"),
            new Category("Wallets", "men_accessories_wallets", 3, "men_accessories"),

            // Outerwear
            new Category("Jackets", "men_outerwear_jackets", 3, "men_outerwear"),
            new Category("Coats", "men_outerwear_coats", 3, "men_outerwear"),
            new Category("Blazers", "men_outerwear_blazers", 3, "men_outerwear"),
            new Category("Vests", "men_outerwear_vests", 3, "men_outerwear"),
            new Category("Trench Coats", "men_outerwear_trench", 3, "men_outerwear"),
            new Category("Parkas", "men_outerwear_parkas", 3, "men_outerwear"),

            // Sportswear
            new Category("Sports Shoes", "men_sportswear_shoes", 3, "men_sportswear"),
            new Category("Track Pants", "men_sportswear_trackpants", 3, "men_sportswear"),
            new Category("Jerseys", "men_sportswear_jerseys", 3, "men_sportswear"),
            new Category("Sweatshirts", "men_sportswear_sweatshirts", 3, "men_sportswear"),
            new Category("Compression Wear", "men_sportswear_compression", 3, "men_sportswear"),
            new Category("Gym Shorts", "men_sportswear_gymshorts", 3, "men_sportswear")
    );



        public static final List<Category> WOMEN_LEVEL_THREE = List.of(
                // Tops
                new Category("Blouses", "women_tops_blouses", 3, "women_tops"),
                new Category("T-Shirts", "women_tops_tshirts", 3, "women_tops"),
                new Category("Tank Tops", "women_tops_tanktops", 3, "women_tops"),
                new Category("Sweaters", "women_tops_sweaters", 3, "women_tops"),
                new Category("Hoodies", "women_tops_hoodies", 3, "women_tops"),
                new Category("Crop Tops", "women_tops_croptops", 3, "women_tops"),

                // Bottoms
                new Category("Jeans", "women_bottoms_jeans", 3, "women_bottoms"),
                new Category("Leggings", "women_bottoms_leggings", 3, "women_bottoms"),
                new Category("Skirts", "women_bottoms_skirts", 3, "women_bottoms"),
                new Category("Trousers", "women_bottoms_trousers", 3, "women_bottoms"),
                new Category("Shorts", "women_bottoms_shorts", 3, "women_bottoms"),
                new Category("Palazzo Pants", "women_bottoms_palazzo", 3, "women_bottoms"),

                // Footwear
                new Category("Heels", "women_footwear_heels", 3, "women_footwear"),
                new Category("Flats", "women_footwear_flats", 3, "women_footwear"),
                new Category("Sneakers", "women_footwear_sneakers", 3, "women_footwear"),
                new Category("Boots", "women_footwear_boots", 3, "women_footwear"),
                new Category("Wedges", "women_footwear_wedges", 3, "women_footwear"),
                new Category("Sandals", "women_footwear_sandals", 3, "women_footwear"),

                // Accessories
                new Category("Handbags", "women_accessories_handbags", 3, "women_accessories"),
                new Category("Earrings", "women_accessories_earrings", 3, "women_accessories"),
                new Category("Necklaces", "women_accessories_necklaces", 3, "women_accessories"),
                new Category("Scarves", "women_accessories_scarves", 3, "women_accessories"),
                new Category("Bracelets", "women_accessories_bracelets", 3, "women_accessories"),
                new Category("Sunglasses", "women_accessories_sunglasses", 3, "women_accessories"),

                // Dresses
                new Category("Casual Dresses", "women_dresses_casual", 3, "women_dresses"),
                new Category("Evening Dresses", "women_dresses_evening", 3, "women_dresses"),
                new Category("Cocktail Dresses", "women_dresses_cocktail", 3, "women_dresses"),
                new Category("Maxi Dresses", "women_dresses_maxi", 3, "women_dresses"),
                new Category("Bodycon Dresses", "women_dresses_bodycon", 3, "women_dresses"),
                new Category("Wrap Dresses", "women_dresses_wrap", 3, "women_dresses"),

                // Sportswear
                new Category("Sports Bras", "women_sportswear_sportsbras", 3, "women_sportswear"),
                new Category("Leggings", "women_sportswear_leggings", 3, "women_sportswear"),
                new Category("Tank Tops", "women_sportswear_tanktops", 3, "women_sportswear"),
                new Category("Yoga Pants", "women_sportswear_yogapants", 3, "women_sportswear"),
                new Category("Running Shoes", "women_sportswear_runningshoes", 3, "women_sportswear"),
                new Category("Tracksuits", "women_sportswear_tracksuits", 3, "women_sportswear")
        );

    public static final List<Category> HOME_FURNITURE_LEVEL_THREE = List.of(
            // Living Room
            new Category("Sofas", "home_living_room_sofas", 3, "home_living_room"),
            new Category("Coffee Tables", "home_living_room_coffee_tables", 3, "home_living_room"),
            new Category("TV Stands", "home_living_room_tv_stands", 3, "home_living_room"),
            new Category("Recliners", "home_living_room_recliners", 3, "home_living_room"),
            new Category("Bookshelves", "home_living_room_bookshelves", 3, "home_living_room"),
            new Category("Accent Chairs", "home_living_room_accent_chairs", 3, "home_living_room"),

            // Bedroom
            new Category("Beds", "home_bedroom_beds", 3, "home_bedroom"),
            new Category("Wardrobes", "home_bedroom_wardrobes", 3, "home_bedroom"),
            new Category("Dressers", "home_bedroom_dressers", 3, "home_bedroom"),
            new Category("Nightstands", "home_bedroom_nightstands", 3, "home_bedroom"),
            new Category("Vanity Tables", "home_bedroom_vanity_tables", 3, "home_bedroom"),
            new Category("Mattresses", "home_bedroom_mattresses", 3, "home_bedroom"),

            // Kitchen & Dining
            new Category("Dining Tables", "home_kitchen_dining_tables", 3, "home_kitchen_dining"),
            new Category("Dining Chairs", "home_kitchen_dining_chairs", 3, "home_kitchen_dining"),
            new Category("Bar Stools", "home_kitchen_dining_bar_stools", 3, "home_kitchen_dining"),
            new Category("Cabinets", "home_kitchen_dining_cabinets", 3, "home_kitchen_dining"),
            new Category("Sideboards", "home_kitchen_dining_sideboards", 3, "home_kitchen_dining"),
            new Category("Kitchen Islands", "home_kitchen_dining_islands", 3, "home_kitchen_dining"),

            // Storage & Organization
            new Category("Closet Organizers", "home_storage_organization_closets", 3, "home_storage_organization"),
            new Category("Shoe Racks", "home_storage_organization_shoe_racks", 3, "home_storage_organization"),
            new Category("Storage Boxes", "home_storage_organization_boxes", 3, "home_storage_organization"),
            new Category("Shelving Units", "home_storage_organization_shelves", 3, "home_storage_organization"),
            new Category("Laundry Baskets", "home_storage_organization_laundry", 3, "home_storage_organization"),
            new Category("Wall Hooks", "home_storage_organization_hooks", 3, "home_storage_organization"),

            // Home Decor
            new Category("Wall Art", "home_decor_wall_art", 3, "home_decor"),
            new Category("Rugs", "home_decor_rugs", 3, "home_decor"),
            new Category("Lamps & Lighting", "home_decor_lamps", 3, "home_decor"),
            new Category("Curtains", "home_decor_curtains", 3, "home_decor"),
            new Category("Vases", "home_decor_vases", 3, "home_decor"),
            new Category("Mirrors", "home_decor_mirrors", 3, "home_decor"),

            // Outdoor & Garden
            new Category("Patio Furniture", "home_outdoor_garden_patio", 3, "home_outdoor_garden"),
            new Category("Outdoor Chairs", "home_outdoor_garden_chairs", 3, "home_outdoor_garden"),
            new Category("Garden Benches", "home_outdoor_garden_benches", 3, "home_outdoor_garden"),
            new Category("Umbrellas & Shades", "home_outdoor_garden_umbrellas", 3, "home_outdoor_garden"),
            new Category("Planters", "home_outdoor_garden_planters", 3, "home_outdoor_garden"),
            new Category("Hammocks", "home_outdoor_garden_hammocks", 3, "home_outdoor_garden")
    );
    public static final List<Category> ELECTRONICS_LEVEL_THREE = List.of(
            // Mobile Phones
            new Category("Smartphones", "electronics_mobile_smartphones", 3, "electronics_mobile_phones"),
            new Category("Feature Phones", "electronics_mobile_feature_phones", 3, "electronics_mobile_phones"),
            new Category("Phone Accessories", "electronics_mobile_accessories", 3, "electronics_mobile_phones"),
            new Category("Power Banks", "electronics_mobile_power_banks", 3, "electronics_mobile_phones"),
            new Category("Screen Protectors", "electronics_mobile_screen_protectors", 3, "electronics_mobile_phones"),
            new Category("Phone Cases", "electronics_mobile_phone_cases", 3, "electronics_mobile_phones"),

            // Laptops & Computers
            new Category("Laptops", "electronics_laptops", 3, "electronics_laptops_computers"),
            new Category("Desktop PCs", "electronics_desktop_pcs", 3, "electronics_laptops_computers"),
            new Category("PC Components", "electronics_pc_components", 3, "electronics_laptops_computers"),
            new Category("Monitors", "electronics_monitors", 3, "electronics_laptops_computers"),
            new Category("Keyboards & Mice", "electronics_keyboards_mice", 3, "electronics_laptops_computers"),
            new Category("External Storage", "electronics_external_storage", 3, "electronics_laptops_computers"),

            // TVs & Home Entertainment
            new Category("LED TVs", "electronics_tvs_led", 3, "electronics_tvs_home_entertainment"),
            new Category("Smart TVs", "electronics_tvs_smart", 3, "electronics_tvs_home_entertainment"),
            new Category("Home Theater Systems", "electronics_home_theater", 3, "electronics_tvs_home_entertainment"),
            new Category("Projectors", "electronics_projectors", 3, "electronics_tvs_home_entertainment"),
            new Category("Streaming Devices", "electronics_streaming_devices", 3, "electronics_tvs_home_entertainment"),
            new Category("TV Accessories", "electronics_tv_accessories", 3, "electronics_tvs_home_entertainment"),

            // Cameras & Accessories
            new Category("DSLR Cameras", "electronics_dslr_cameras", 3, "electronics_cameras_accessories"),
            new Category("Mirrorless Cameras", "electronics_mirrorless_cameras", 3, "electronics_cameras_accessories"),
            new Category("Action Cameras", "electronics_action_cameras", 3, "electronics_cameras_accessories"),
            new Category("Camera Lenses", "electronics_camera_lenses", 3, "electronics_cameras_accessories"),
            new Category("Tripods & Mounts", "electronics_tripods_mounts", 3, "electronics_cameras_accessories"),
            new Category("Memory Cards", "electronics_memory_cards", 3, "electronics_cameras_accessories"),

            // Audio Devices
            new Category("Headphones", "electronics_audio_headphones", 3, "electronics_audio_devices"),
            new Category("Wireless Earbuds", "electronics_audio_wireless_earbuds", 3, "electronics_audio_devices"),
            new Category("Bluetooth Speakers", "electronics_audio_bluetooth_speakers", 3, "electronics_audio_devices"),
            new Category("Soundbars", "electronics_audio_soundbars", 3, "electronics_audio_devices"),
            new Category("Microphones", "electronics_audio_microphones", 3, "electronics_audio_devices"),
            new Category("Audio Interfaces", "electronics_audio_interfaces", 3, "electronics_audio_devices"),

            // Smart Home Devices
            new Category("Smart Speakers", "electronics_smart_speakers", 3, "electronics_smart_home_devices"),
            new Category("Smart Lighting", "electronics_smart_lighting", 3, "electronics_smart_home_devices"),
            new Category("Security Cameras", "electronics_smart_security_cameras", 3, "electronics_smart_home_devices"),
            new Category("Smart Plugs", "electronics_smart_plugs", 3, "electronics_smart_home_devices"),
            new Category("Smart Thermostats", "electronics_smart_thermostats", 3, "electronics_smart_home_devices"),
            new Category("Smart Locks", "electronics_smart_locks", 3, "electronics_smart_home_devices")
    );
}
