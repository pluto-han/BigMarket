package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RuleTreeNodeLine;

import java.util.List;

/**
 * @program: BigMarket
 * @description:
 * @author: Hancong Zhang
 * @create: 11/14/25 6:14 PM
 **/
@Mapper
public interface IRuleTreeNodeLineDao {

    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(String treeId);
}
