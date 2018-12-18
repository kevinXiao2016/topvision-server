/**
 *
 */
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.PostDao;
import com.topvision.platform.domain.Post;

/**
 * @author niejun
 */
@Repository("postDao")
public class PostDaoImpl extends MyBatisDaoSupport<Post> implements PostDao {
    @Override
    public String getDomainName() {
        return Post.class.getName();
    }
}
