package com.bank.account.models;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_details", schema = "account")
public class AccountDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NonNull
    @Column(name = "passport_id")
    private long  passportId;

    @NonNull
    @Column(name = "account_number")
    private long accountNumber;

    @NonNull
    @Column(name = "bank_details_id")
    private long bankDetailsId;

    @NonNull
    @Column(precision = 20, scale = 2)
    private BigDecimal money;

    @NonNull
    @Column(name = "negative_balance")
    private boolean negativeBalance;

    @NonNull
    @Column(name = "profile_id")
    private long profileId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Audit audit;

}
