package com.topvision.ems.mibble.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.mibble.service.MibbleLoader;
import com.topvision.framework.service.BaseService;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.browser.MibNode;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.SequenceOfType;
import net.percederberg.mibble.type.SequenceType;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Service
public class MibbleLoaderImpl extends BaseService implements MibbleLoader {
    /**
     * The MIB loader to use.
     */
    @Autowired
    private MibLoader loader;

    /**
     * 解析一个MIB对象成一个JSON对象
     * 
     * @param root
     * @param json
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @Override
    public void parseMib(MibNode root, JSONObject json) throws JSONException, UnsupportedEncodingException {
        int count = root.getChildCount();
        JSONArray array = new JSONArray();
        for (int i = 0; i < count; i++) {
            MibNode node = (MibNode) root.getChildAt(i);
            JSONObject o = new JSONObject();
            o.put("name", node.getName());
            o.put("icon", getIcon(node, node.isLeaf()));
            o.put("writeable", isWriteable(node, node.isLeaf()));
            o.put("symbol", node.getSymbol().toString());
            o.put("oid", node.getOid());
            o.put("isLeaf", node.isLeaf());
            if (node.getChildCount() > 0)
                parseMib(node, o);
            array.add(o);
        }
        json.put("children", array);

    }

    /**
     * 判断节点是否可写
     * 
     * @param node
     * @param leaf
     * @return
     */
    @SuppressWarnings("unchecked")
    private boolean isWriteable(MibNode node, boolean leaf) {
        SnmpObjectType type = node.getSnmpObjectType();
        if (leaf) {
            if (type == null) {
                return false;
            } else {
                try {
                    MibNode parent = (MibNode) node.getParent();
                    List<SnmpIndex> index = parent.getSnmpObjectType().getIndex();
                    for (SnmpIndex si : index) {
                        if (si.getValue().toString().equals(node.getOid())) {
                            return false;
                        }
                    }
                    if (type.getAccess().canWrite()) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception ex) {
                    if (type.getAccess().canWrite()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
    }

    /**
     * 得到节点图标
     * 
     * @param node
     * @param leaf
     * @return
     */
    @SuppressWarnings("unchecked")
    private String getIcon(MibNode node, boolean leaf) {
        SnmpObjectType type = node.getSnmpObjectType();
        if (leaf) {
            if (type == null) {
                return "images/folderc.jpg";
            } else {
                try {
                    MibNode parent = (MibNode) node.getParent();
                    ArrayList<SnmpIndex> index = parent.getSnmpObjectType().getIndex();
                    for (SnmpIndex si : index) {
                        if (si.getValue().toString().equals(node.getOid())) {
                            return "images/index.jpg";
                        }
                    }
                    if (type.getAccess().canWrite()) {
                        return "images/leaf.jpg";
                    } else {
                        return "images/noaccess.jpg";
                    }
                } catch (Exception ex) {
                    if (type.getAccess().canWrite()) {
                        return "images/leaf.jpg";
                    } else {
                        return "images/noaccess.jpg";
                    }
                }
            }
        } else if (type != null && type.getSyntax() != null && type.getSyntax() instanceof SequenceType) {
            return "images/entry.jpg";
        } else if (type != null && type.getSyntax() != null && type.getSyntax() instanceof SequenceOfType) {
            return "images/table.jpg";
        }
        return null;
    }

    /**
     * Loads MIB file or URL.
     * 
     * @param src
     *            the MIB file or URL
     * @return
     * 
     * @throws IOException
     *             if the MIB file couldn't be found in the MIB search path
     * @throws MibLoaderException
     *             if the MIB file couldn't be loaded correctly
     */
    @Override
    public Mib loadMib(String src) throws IOException, MibLoaderException {
        // MibTreeBuilder mb = MibTreeBuilder.getInstance();
        File file = new File(src);
        Mib mib = null;

        if (file.exists()) {
            /*
             * if (loader.getMib(file) != null) { //由于现在mib没有存在mibs列表中,故需要返回 return null; }
             */
            if (!loader.hasDir(file.getParentFile())) {
                loader.removeAllDirs();
                loader.addDir(file.getParentFile());
            }
            mib = loader.load(file);
        } else {
            mib = loader.load(src);
        }
        return mib;
    }

    /**
     * parse a MIB to the MIB tree json.
     * 
     * @param mib
     *            the MIB to parse
     */
    @SuppressWarnings("rawtypes")
    public JTree parse(Mib mib) {
        Iterator iter = mib.getAllSymbols().iterator();
        MibSymbol symbol;
        MibNode node;
        JTree valueTree;

        // Create value sub tree
        node = new MibNode(mib.getName(), null);
        valueTree = new JTree(node);
        while (iter.hasNext()) {
            symbol = (MibSymbol) iter.next();
            addSymbol(valueTree.getModel(), symbol);
        }

        // Add sub tree root to MIB tree
        node = new MibNode(mib.getName(), null);
        return valueTree;
    }

    /**
     * Adds a MIB symbol to a MIB tree model.
     * 
     * @param model
     *            the MIB tree model
     * @param symbol
     *            the MIB symbol
     * 
     * @see #addToTree
     */
    private void addSymbol(TreeModel model, MibSymbol symbol) {
        MibValue value;
        ObjectIdentifierValue oid;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                oid = (ObjectIdentifierValue) value;
                addToTree(model, oid);
            }
        }
    }

    /**
     * Adds an object identifier value to a MIB tree model.
     * 
     * @param model
     *            the MIB tree model
     * @param oid
     *            the object identifier value
     * 
     * @return the MIB tree node added
     */
    private MibNode addToTree(TreeModel model, ObjectIdentifierValue oid) {
        MibNode parent;
        MibNode node;
        String name;

        // Add parent node to tree (if needed)
        if (hasParent(oid)) {
            parent = addToTree(model, oid.getParent());
        } else {
            parent = (MibNode) model.getRoot();
        }

        // Check if node already added
        for (int i = 0; i < model.getChildCount(parent); i++) {
            node = (MibNode) model.getChild(parent, i);
            if (node.getValue().equals(oid)) {
                return node;
            }
        }

        // Create new node
        name = oid.getName() + " (" + oid.getValue() + ")";
        node = new MibNode(name, oid);
        parent.add(node);
        // 相当于一个所有节点的目录。可以快速定位节点
        // nodes.put(oid.getSymbol(), node);
        return node;
    }

    /**
     * Checks if the specified object identifier has a parent.
     * 
     * @param oid
     *            the object identifier to check
     * 
     * @return true if the object identifier has a parent, or false otherwise
     */
    private boolean hasParent(ObjectIdentifierValue oid) {
        ObjectIdentifierValue parent = oid.getParent();

        return oid.getSymbol() != null && oid.getSymbol().getMib() != null && parent != null
                && parent.getSymbol() != null && parent.getSymbol().getMib() != null
                && parent.getSymbol().getMib().equals(oid.getSymbol().getMib());
    }

    /**
     * @return the loader
     */
    public MibLoader getLoader() {
        return loader;
    }

    /**
     * @param loader
     *            the loader to set
     */
    public void setLoader(MibLoader loader) {
        this.loader = loader;
    }

}
