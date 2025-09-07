package entity;

import jakarta.persistence.*;

@Entity
@Table(name ="categories")
public class Category implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cateId;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String cateName;
    private String cateIcon;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category(int cateId, String cateName, String cateIcon) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.cateIcon = cateIcon;
    }

    public Category() {
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCateIcon() {
        return cateIcon;
    }

    public void setCateIcon(String cateIcon) {
        this.cateIcon = cateIcon;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
