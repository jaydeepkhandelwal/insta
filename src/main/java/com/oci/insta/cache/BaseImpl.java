package com.oci.insta.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redisson.api.RMap;

import javax.persistence.MappedSuperclass;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Data
public abstract class BaseImpl <K,V> {

    RMap<K,V> map;

    public void putToken(K k,V v){
        map.putIfAbsent(k, v);
    }

    public V getTokenByKey(K k){
        return map.get(k);
    }

    public void deleteToken(K k){
        map.remove(k);
    }
}