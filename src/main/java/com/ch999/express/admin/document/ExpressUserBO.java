package com.ch999.express.admin.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * @author hahalala
 */
@RedisHash("ExpressUserBO")
@Data
@NoArgsConstructor
@ToString
public class ExpressUserBO {

    @Id
    private String authorization;

    private Integer userId;

    @TimeToLive
    long liveTime = 86400L;

    public ExpressUserBO(String authorization, Integer userId) {
        this.authorization = authorization;
        this.userId = userId;
    }
}
