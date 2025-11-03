package com.xs.springboot.mapper;

import com.xs.springboot.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository//理论上用component也可以，但是这个表示数据访问层的bean，职责更加明确
public interface UserMapper extends CrudRepository<User, Integer>{


}
