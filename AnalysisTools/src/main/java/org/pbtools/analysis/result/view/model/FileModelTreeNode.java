package org.pbtools.analysis.result.view.model;

import org.zkoss.zul.DefaultTreeNode;
 
public class FileModelTreeNode extends DefaultTreeNode<FileModel> {
    private static final long serialVersionUID = -7012663776755277499L;
     
    private boolean open = false;
 
    public FileModelTreeNode(FileModel data, DefaultTreeNode<FileModel>[] children) {
        super(data, children);
    }
 
    public FileModelTreeNode(FileModel data, DefaultTreeNode<FileModel>[] children, boolean open) {
        super(data, children);
        setOpen(open);
    }
 
    public FileModelTreeNode(FileModel data) {
        super(data);
 
    }
 
    public boolean isOpen() {
        return open;
    }
 
    public void setOpen(boolean open) {
        this.open = open;
    }
 
}