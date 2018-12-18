package com.topvision.platform.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.exception.service.ExistException;
import com.topvision.framework.domain.TreeNode;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.Post;
import com.topvision.platform.service.SystemService;
import com.topvision.platform.service.UserService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller("postAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostAction extends BaseAction {
    private static final long serialVersionUID = -2737726808548795905L;
    @Autowired
    private SystemService systemService = null;
    @Autowired
    private UserService userService = null;
    private long postId;
    private Post post = null;
    private long superiorId;

    /**
     * 创建标记
     * 
     * @return
     */
    public String createPost() throws Exception {
        JSONObject json = new JSONObject();
        try {
            systemService.createPost(post);
            json.put("exists", false);
        } catch (ExistException ee) {
            json.put("exists", true);
        } finally {
            writeDataToAjax(json);
        }
        return NONE;
    }

    /**
     * 删除标记
     * 
     * @return
     */
    public String deletePost() throws Exception {
        JSONObject json = new JSONObject();
        Integer count = userService.getPostUsedCount(postId);
        if (postId > 0) {
            if (count > 0) {
                json.put("exists", true);
            } else {
                systemService.removePost(postId);
            }
        }
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 载入所有标记
     * 
     * @return
     * @throws Exception
     */
    public String loadAllPost() throws Exception {
        List<TreeNode> array = new ArrayList<TreeNode>();
        HashMap<String, TreeNode> map = new HashMap<String, TreeNode>();
        List<Post> list = systemService.getAllPost();
        for (int i = 0; i < list.size(); i++) {
            Post post = list.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setText(post.getName());
            treeNode.setId(post.getPostId());
            treeNode.setChildren(new ArrayList<TreeNode>());
            treeNode.setIconCls("postIcon");
            map.put(String.valueOf(post.getPostId()), treeNode);
            TreeNode parent = map.get(String.valueOf(post.getSuperiorId()));
            if (parent == null) {
                array.add(treeNode);
            } else {
                parent.getChildren().add(treeNode);
            }
        }
        writeDataToAjax(array);
        return NONE;
    }

    /**
     * 显示新建标记
     * 
     * @return
     */
    public String showNewPost() {
        return SUCCESS;
    }

    public Post getPost() {
        return post;
    }

    public long getPostId() {
        return postId;
    }

    public long getSuperiorId() {
        return superiorId;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setSuperiorId(long superiorId) {
        this.superiorId = superiorId;
    }

    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }

    public SystemService getSystemService() {
        return systemService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
