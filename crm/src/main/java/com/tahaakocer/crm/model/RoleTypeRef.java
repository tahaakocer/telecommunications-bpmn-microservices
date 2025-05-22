package com.tahaakocer.crm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "role_type_ref", schema = "party_role_management")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleTypeRef extends BaseEntity{
    private String name;

}
