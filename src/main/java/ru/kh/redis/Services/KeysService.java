package ru.kh.redis.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kh.redis.Repositories.CacheRepository;

import java.util.List;

@Service
public class KeysService {

    private final CacheRepository cacheRepository;

    @Autowired
    public KeysService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public int delKeys(String[] keys) {
        return cacheRepository.removesKeys(keys);
    }

    public String getKeys(String pattern) {
        StringBuilder result = new StringBuilder();
        List<String> keys = cacheRepository.getKeysByPattern(pattern);
        if (keys == null || keys.size() == 0) {
            return result.append("No matchers").toString();
        }
        for (int i = 0; i < keys.size(); i++) {
            result
                    .append((i + 1))
                    .append(" ) ")
                    .append(keys.get(i))
                    .append("\n");
        }
        return result.toString();
    }
}
