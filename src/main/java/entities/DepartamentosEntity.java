package entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "DEPARTAMENTOS", schema = "ROOT", catalog = "")
public class DepartamentosEntity {
    @Id
    @Column(name = "DEPT_NO")
    private int deptNo;
    @Basic
    @Column(name = "DNOMBRE")
    private String dnombre;
    @Basic
    @Column(name = "LOC")
    private String loc;

    public int getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(int deptNo) {
        this.deptNo = deptNo;
    }

    public String getDnombre() {
        return dnombre;
    }

    public void setDnombre(String dnombre) {
        this.dnombre = dnombre;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartamentosEntity that = (DepartamentosEntity) o;
        return deptNo == that.deptNo && Objects.equals(dnombre, that.dnombre) && Objects.equals(loc, that.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptNo, dnombre, loc);
    }
}
