package com.ch999.express.admin.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author hahalala
 */
@RedisHash("UserWalletBO")
@Data
@NoArgsConstructor
public class UserWalletBO {

    @Id
    private Integer userId;

    private Double balance;

    private Integer integral;

    public UserWalletBO(Integer userId) {
        this.userId = userId;
        this.balance = 0.0;
        this.integral = 0;
    }
}
