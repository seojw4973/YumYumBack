package org.baratie.yumyum.domain.member.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValue(String key, String value) {

        redisTemplate.opsForValue().set(key, value);
    }

    public Object getValue(String key) {

        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key){

        redisTemplate.delete(key);
    }


}
