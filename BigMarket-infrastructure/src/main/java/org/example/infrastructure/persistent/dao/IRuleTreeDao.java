package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RuleTree;
import org.springframework.stereotype.Repository;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/14/25 6:13 PM
 **/
@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(String treeId);
}
