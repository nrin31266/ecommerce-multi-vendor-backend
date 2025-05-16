package com.vanrin05.app.configuration;

import com.vanrin05.app.data.CategoriesData;
import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.domain.USER_ROLE;
import com.vanrin05.app.model.*;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder encoder;

    private final List<User> initialUsers = List.of(
            new User("rin001@yopmail.com", "John Doe", "0823743824", "123"),
            new User("rin002@yopmail.com", "Alice Smith", "0912837465","123"),
            new User("rin003@yopmail.com", "Bob Johnson", "0812345678","123"),
            new User("rin004@yopmail.com", "Charlie Brown", "0998765432","123"),
            new User("rin005@yopmail.com", "David Williams", "0701234567","123"),
            new User("rin006@yopmail.com", "Emma Taylor", "0698765432","123"),
            new User("rin007@yopmail.com", "Frank Miller", "0612345678","123"),
            new User("rin008@yopmail.com", "Grace Wilson", "0598765432","123"),
            new User("rin009@yopmail.com", "Henry Moore", "0512345678","123"),
            new User("rin010@yopmail.com", "Isla Davis", "0498765432","123")
    );



    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, SellerRepository sellerRepository, CategoryRepository categoryRepository,
                                   VerificationCodeRepository verificationCodeRepository, CartRepository cartRepository, ProductRepository productRepository, SellerReportRepository sellerReportRepository) {
        return args -> {
            if (userRepository.findByEmail("rin001@yopmail.com").isEmpty()) {
                List<User> savedUsers = userRepository.saveAll(initialUsers.stream().peek(user -> user.setPassword(encoder.encode(user.getPassword()))).toList());
                List<VerificationCode> verificationUserCodes = new ArrayList<>();
                List<Cart> userCarts = new ArrayList<>();
                savedUsers.forEach(user -> {
                    verificationUserCodes.add(VerificationCode.builder()
                            .email(user.getEmail())
                            .user(user)
                            .build());
                    userCarts.add(
                            new Cart(user)
                    );
                });
                verificationCodeRepository.saveAll(verificationUserCodes);
                cartRepository.saveAll(userCarts);
                List<Seller> savedSellers = sellerRepository.saveAll(initialSellers.stream().peek(seller -> seller.setPassword(encoder.encode(seller.getPassword()))).toList());

                List<VerificationCode> verificationSellerCodes = new ArrayList<>();
                List<SellerReport> sellerReports = new ArrayList<>();
                savedSellers.forEach(seller -> {
                    verificationSellerCodes.add(VerificationCode.builder()
                            .email(seller.getEmail())
                            .seller(seller)
                            .build());
                    sellerReports.add(SellerReport.builder()
                                    .seller(seller)
                            .build());

                });
                verificationCodeRepository.saveAll(verificationSellerCodes);
                sellerReportRepository.saveAll(sellerReports);
                List<Category> allCategories = new ArrayList<>();
                allCategories.addAll(CategoriesData.MAIN_CATEGORIES);
                allCategories.addAll(CategoriesData.MEN_LEVEL_TWO);
                allCategories.addAll(CategoriesData.MEN_LEVEL_THREE);
                allCategories.addAll(CategoriesData.WOMEN_LEVEL_TWO);
                allCategories.addAll(CategoriesData.WOMEN_LEVEL_THREE);
                allCategories.addAll(CategoriesData.HOME_FURNITURE_LEVEL_TWO);
                allCategories.addAll(CategoriesData.HOME_FURNITURE_LEVEL_THREE);
                allCategories.addAll(CategoriesData.ELECTRONICS_LEVEL_TWO);
                allCategories.addAll(CategoriesData.ELECTRONICS_LEVEL_THREE);

                Map<String, Category> savedCategories = categoryRepository.saveAll(allCategories).stream().collect(Collectors.toMap(
                        Category::getCategoryId, category -> category
                ));



//                List<Product> allProducts = productList(savedCategories, savedSellers.get(0), savedSellers.get(1), savedSellers.get(2));
//
//                productRepository.saveAll(allProducts);




            }
        };
    }

    private final List<Seller> initialSellers = List.of(
            Seller.builder()
                    .email("rin0011@yopmail.com")
                    .sellerName("James Moe")
                    .gstin("GSTIN123456")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX123")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0912345678")
                    .password("123")
                    .acceptTerms(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("James Moe")
                                    .accountNumber("1234567890")
                                    .ifscCode("IFSC1234")
                                    .swiftCode("SWIFT123")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .name("Bob Johnson")
                                    .province("Chicago")
                                    .district("IL")
                                    .phoneNumber("0934567890")
                                    .ward("Downtown")
                                    .postalCode("60601")
                                    .build()
                    ).businessDetails(
                            BusinessDetails.builder()
                                    .banner(null)
                                    .businessAddress("456 Business Road")
                                    .businessEmail("business001@yopmail.com")
                                    .businessMobile("0998765432").businessName("business001")
                                    .logo(null)
                                    .build()
                    ).build(),

            Seller.builder()
                    .email("rin0012@yopmail.com")
                    .sellerName("Alice Smith")
                    .gstin("GSTIN654321").password("123")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX456")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0923456789")
                    .acceptTerms(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Alice Smith")
                                    .accountNumber("2345678901")
                                    .ifscCode("IFSC5678")
                                    .swiftCode("SWIFT456")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .name("Bob Johnson")
                                    .province("Chicago")
                                    .district("IL")
                                    .phoneNumber("0934567890")
                                    .ward("Downtown")
                                    .postalCode("60601")
                                    .build()
                    ).businessDetails(
                            BusinessDetails.builder()
                                    .banner(null)
                                    .businessAddress("123 Commerce Street")
                                    .businessEmail("business002@yopmail.com")
                                    .businessMobile("0987654321").businessName("business002")
                                    .logo(null)
                                    .build()
                    ).build(),

            Seller.builder()
                    .email("rin0013@yopmail.com")
                    .sellerName("Bob Johnson")
                    .gstin("GSTIN789012")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX789")
                    .role(USER_ROLE.ROLE_SELLER).password("123")
                    .mobile("0934567890")
                    .acceptTerms(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Bob Johnson")
                                    .accountNumber("3456789012")
                                    .ifscCode("IFSC7890")
                                    .swiftCode("SWIFT789")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .name("Bob Johnson")
                                    .province("Chicago")
                                    .district("IL")
                                    .phoneNumber("0934567890")
                                    .ward("Downtown")
                                    .postalCode("60601")
                                    .build()
                    ).businessDetails(
                            BusinessDetails.builder()
                                    .banner(null)
                                    .businessName("business001")
                                    .businessAddress("987 Market Lane")
                                    .businessEmail("business003@yopmail.com")
                                    .businessMobile("0976543210").businessName("business003")
                                    .logo(null)
                                    .build()
                    ).build(),

            Seller.builder()
                    .email("rin0014@yopmail.com")
                    .sellerName("Charlie Brown").password("123")
                    .gstin("GSTIN345678")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX234")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0945678901")
                    .acceptTerms(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Charlie Brown")
                                    .accountNumber("4567890123")
                                    .ifscCode("IFSC2345")
                                    .swiftCode("SWIFT234")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .name("Bob Johnson")
                                    .province("Chicago")
                                    .district("IL")
                                    .phoneNumber("0934567890")
                                    .ward("Downtown")
                                    .postalCode("60601")
                                    .build()
                    ).businessDetails(
                            BusinessDetails.builder()
                                    .banner(null)
                                    .businessAddress("654 Business Parkway")
                                    .businessEmail("business004@yopmail.com")
                                    .businessMobile("0965432109")
                                    .logo(null).businessName("business004")
                                    .build()
                    ).build(),

            Seller.builder()
                    .email("rin0015@yopmail.com")
                    .sellerName("David Williams")
                    .gstin("GSTIN567890")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX567").password("123")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0956789012")
                    .acceptTerms(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("David Williams")
                                    .accountNumber("5678901234")
                                    .ifscCode("IFSC5678")
                                    .swiftCode("SWIFT567")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .name("Bob Johnson")
                                    .province("Chicago")
                                    .district("IL")
                                    .phoneNumber("0934567890")
                                    .ward("Downtown")
                                    .postalCode("60601")
                                    .build()
                    ).businessDetails(
                            BusinessDetails.builder()
                                    .banner(null)
                                    .businessAddress("321 Trade Center")
                                    .businessEmail("business005@yopmail.com")
                                    .businessMobile("0954321098").businessName("business005")
                                    .logo(null)
                                    .build()
                    ).build()


    );



//     private List<Product> productList(Map<String, Category> savedCategories, Seller seller1, Seller seller2, Seller seller3) {
//        return Arrays.asList(
//                Product.builder()
//
//                        .title("Heavyweight Tee")
//                        .description("Heavyweight Tee")
//                        .mrpPrice(3000000)
//                        .sellingPrice(1000000)
//                        .discountPercentage(200)
//                        .quantity(321)
//                        .color("#000080")
//                        .sizes("S, M, L, XL, 2XL, 3XL, 4XL")
//                        .images(Arrays.asList(
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510732174-NonBranded_Mens_HeavyweightTee_Navy_Mock_720x.webp?alt=media&token=e6c9afe5-2d1b-4ba6-8e14-05e173ce2e24",
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510732175-NonBranded_Mens_Heavyweight_Tees_Zac_Navy_Front_720x.webp?alt=media&token=bd26dc80-113d-4639-a7db-710ab37243f5"
//                        ))
//                        .numberRating(0)
//                        .category(savedCategories.get("men_tops_tshirts"))
//                        .seller(seller1)
//                        .createdAt(LocalDateTime.parse("2025-03-21T05:45:33.133478"))
//                        .build(),
//
//                Product.builder()
//
//                        .title("Evergreen Vintage Tee")
//                        .description("Evergreen Vintage Tee")
//                        .mrpPrice(1300000)
//                        .sellingPrice(200000)
//                        .discountPercentage(0)
//                        .quantity(12)
//                        .color("#008080")
//                        .sizes("S, M, L, XL, 2XL, 3XL, 4XL")
//                        .images(Arrays.asList(
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510797626-Evergreen_Mens_Tee_Mock_Front_2048x.webp?alt=media&token=a07adef0-0b25-4e25-ab01-9aaa2403a95e",
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510797627-Evergreen_Mens_Tee_Front_2048x.webp?alt=media&token=9978573a-3152-4085-8634-79ab3969216f"
//                        ))
//                        .numberRating(0)
//                        .category(
//                               savedCategories.get("men_tops_tshirts"))
//                        .seller(seller2)
//                        .createdAt(LocalDateTime.parse("2025-03-21T05:46:40.231542"))
//                        .build(),
//                Product.builder()
//
//                        .title("Polo - Branded")
//                        .description("Polo - Branded")
//                        .mrpPrice(400000)
//                        .sellingPrice(50000)
//                        .discountPercentage(0)
//                        .quantity(3)
//                        .color("#0000FF")
//                        .sizes("S, M, L, XL, 2XL, 3XL, 4XL")
//                        .images(Arrays.asList(
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742511367126-Branded_Mens_Polos_RoyalBlue_Front_720x.webp?alt=media&token=eabc2e3f-76fb-4b91-b366-a62229667f72",
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742511367127-Mens_SSPolo_Branded_RoyalBlue_Front.jpg?alt=media&token=b8a9f5d8-502c-4bf3-a95d-aecae905c564"
//                        ))
//                        .numberRating(0)
//                        .category(savedCategories.get("men_tops_polo"))
//                        .seller(seller3)
//                        .createdAt(LocalDateTime.parse("2025-03-21T05:56:10.180944"))
//                        .build(),
//                Product.builder()
//
//                        .title("Pullover Hoodie (Classic Pocket) - Non-Branded")
//                        .description("Pullover Hoodie (Classic Pocket) - Non-Branded")
//                        .mrpPrice(1000000)
//                        .sellingPrice(300000)
//                        .discountPercentage(0)
//                        .quantity(12)
//                        .color("#000000")
//                        .sizes("S, M, L, XL, 2XL, 3XL, 4XL")
//                        .images(Arrays.asList(
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510689621-PulloverHoodie_Mens_NonBranded_Black_Front_2048x.webp?alt=media&token=d0308739-f055-4566-9d9d-a0dd34eed7c3",
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510689622-KangarooNB_Mens_Hoodie_Black_Front_2048x.webp?alt=media&token=4d5994e9-3d5e-48cc-a4c2-b140908d46cc",
//                                "https://firebasestorage.googleapis.com/v0/b/crested-mercury-452707-e8.firebasestorage.app/o/ecommerce_project1%2Fimages%2F1742510689622-KangarooNB_Mens_Hoodie_Black_Back.jpg?alt=media&token=d264bf4f-8121-45f5-b4b9-b4519b6116f1"
//                        ))
//                        .numberRating(0)
//                        .category(savedCategories.get("men_tops_hoodies"))
//                        .seller(seller1)
//                        .createdAt(LocalDateTime.parse("2025-03-21T05:44:50.746251"))
//                        .build()
//        );
//    }

}
