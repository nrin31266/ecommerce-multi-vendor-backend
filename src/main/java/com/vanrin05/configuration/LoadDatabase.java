package com.vanrin05.configuration;

import com.vanrin05.data.CategoriesData;
import com.vanrin05.domain.ACCOUNT_STATUS;
import com.vanrin05.domain.USER_ROLE;
import com.vanrin05.model.*;
import com.vanrin05.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDatabase {
    private final PasswordEncoder encoder;

    private final List<User> initialUsers = List.of(
            new User("rin001@yopmail.com", "John Doe", "0823743824"),
            new User("rin002@yopmail.com", "Alice Smith", "0912837465"),
            new User("rin003@yopmail.com", "Bob Johnson", "0812345678"),
            new User("rin004@yopmail.com", "Charlie Brown", "0998765432"),
            new User("rin005@yopmail.com", "David Williams", "0701234567"),
            new User("rin006@yopmail.com", "Emma Taylor", "0698765432"),
            new User("rin007@yopmail.com", "Frank Miller", "0612345678"),
            new User("rin008@yopmail.com", "Grace Wilson", "0598765432"),
            new User("rin009@yopmail.com", "Henry Moore", "0512345678"),
            new User("rin010@yopmail.com", "Isla Davis", "0498765432")
    );


    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, SellerRepository sellerRepository, CategoryRepository categoryRepository,
                                   VerificationCodeRepository verificationCodeRepository, CartRepository cartRepository) {
        return args -> {
            if (userRepository.findByEmail("rin001@yopmail.com").isEmpty()) {
                List<User> savedUsers = userRepository.saveAll(initialUsers);
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
                List<Seller> savedSellers = sellerRepository.saveAll(initialSellers);
                List<VerificationCode> verificationSellerCodes = new ArrayList<>();
                savedSellers.forEach(seller -> {
                    verificationSellerCodes.add(VerificationCode.builder()
                            .email(seller.getEmail())
                            .seller(seller)
                            .build());
                });
                verificationCodeRepository.saveAll(verificationSellerCodes);
                categoryRepository.saveAll(CategoriesData.MAIN_CATEGORIES);
                categoryRepository.saveAll(CategoriesData.MEN_LEVEL_TWO);
                categoryRepository.saveAll(CategoriesData.MEN_LEVEL_THREE);
                categoryRepository.saveAll(CategoriesData.WOMEN_LEVEL_TWO);
                categoryRepository.saveAll(CategoriesData.WOMEN_LEVEL_THREE);
                categoryRepository.saveAll(CategoriesData.ELECTRONICS_LEVEL_TWO);
                categoryRepository.saveAll(CategoriesData.ELECTRONICS_LEVEL_THREE);

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
                    .isEmailVerified(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("James Moe")
                                    .accountNumber("1234567890")
                                    .ifscCode("IFSC1234")
                                    .swiftCode("SWIFT123")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .address("123 Street, District 1")
                                    .name("James Moe")
                                    .city("New York")
                                    .state("NY")
                                    .mobile("0912345678")
                                    .locality("Downtown")
                                    .pinCode("10001")
                                    .zipCode("10001")
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
                    .gstin("GSTIN654321")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX456")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0923456789")
                    .isEmailVerified(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Alice Smith")
                                    .accountNumber("2345678901")
                                    .ifscCode("IFSC5678")
                                    .swiftCode("SWIFT456")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .address("789 Avenue, District 2")
                                    .name("Alice Smith")
                                    .city("Los Angeles")
                                    .state("CA")
                                    .mobile("0923456789")
                                    .locality("Beverly Hills")
                                    .pinCode("90001")
                                    .zipCode("90001")
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
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0934567890")
                    .isEmailVerified(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Bob Johnson")
                                    .accountNumber("3456789012")
                                    .ifscCode("IFSC7890")
                                    .swiftCode("SWIFT789")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .address("101 Main Street")
                                    .name("Bob Johnson")
                                    .city("Chicago")
                                    .state("IL")
                                    .mobile("0934567890")
                                    .locality("Downtown")
                                    .pinCode("60601")
                                    .zipCode("60601")
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
                    .sellerName("Charlie Brown")
                    .gstin("GSTIN345678")
                    .accountStatus(ACCOUNT_STATUS.ACTIVE)
                    .taxCode("TAX234")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0945678901")
                    .isEmailVerified(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("Charlie Brown")
                                    .accountNumber("4567890123")
                                    .ifscCode("IFSC2345")
                                    .swiftCode("SWIFT234")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .address("222 Elm Street")
                                    .name("Charlie Brown")
                                    .city("Houston")
                                    .state("TX")
                                    .mobile("0945678901")
                                    .locality("Midtown")
                                    .pinCode("77001")
                                    .zipCode("77001")
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
                    .taxCode("TAX567")
                    .role(USER_ROLE.ROLE_SELLER)
                    .mobile("0956789012")
                    .isEmailVerified(true)
                    .bankDetails(
                            BankDetails.builder()
                                    .accountHolderName("David Williams")
                                    .accountNumber("5678901234")
                                    .ifscCode("IFSC5678")
                                    .swiftCode("SWIFT567")
                                    .build()
                    ).pickupAddress(
                            Address.builder()
                                    .address("333 Oak Drive")
                                    .name("David Williams")
                                    .city("San Francisco")
                                    .state("CA")
                                    .mobile("0956789012")
                                    .locality("Sunset District")
                                    .pinCode("94101")
                                    .zipCode("94101")
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

}
