package com.vanrin05.app.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionValue;
    @ManyToOne
    @JoinColumn(name = "product_option_type_id")
    private ProductOptionType optionType;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "sub_product_id")
    private SubProduct subProduct;


}
