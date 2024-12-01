package entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "EMPLEADOS", schema = "ROOT", catalog = "")
public class EmpleadosEntity {
    @Id
    @Column(name = "EMP_NO")
    private int empNo;
    @Basic
    @Column(name = "APELLIDO")
    private String apellido;
    @Basic
    @Column(name = "OFICIO")
    private String oficio;
    @Basic
    @Column(name = "DIR")
    private Integer dir;
    @Basic
    @Column(name = "FECHA_ALT")
    private Date fechaAlt;
    @Basic
    @Column(name = "SALARIO")
    private Double salario;
    @Basic
    @Column(name = "COMISION")
    private Double comision;
    @ManyToOne (cascade = {CascadeType.MERGE})
    @JoinColumn(name = "DEPT_NO", referencedColumnName = "DEPT_NO", nullable = false)
    private DepartamentosEntity departamentosByDeptNo;

    public int getEmpNo() {
        return empNo;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Integer getDir() {
        return dir;
    }

    public void setDir(Integer dir) {
        this.dir = dir;
    }

    public Date getFechaAlt() {
        return fechaAlt;
    }

    public void setFechaAlt(Date fechaAlt) {
        this.fechaAlt = fechaAlt;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpleadosEntity that = (EmpleadosEntity) o;
        return empNo == that.empNo && Objects.equals(apellido, that.apellido) && Objects.equals(oficio, that.oficio) && Objects.equals(dir, that.dir) && Objects.equals(fechaAlt, that.fechaAlt) && Objects.equals(salario, that.salario) && Objects.equals(comision, that.comision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empNo, apellido, oficio, dir, fechaAlt, salario, comision);
    }

    public DepartamentosEntity getDepartamentosByDeptNo() {
        return departamentosByDeptNo;
    }

    public void setDepartamentosByDeptNo(DepartamentosEntity departamentosByDeptNo) {
        this.departamentosByDeptNo = departamentosByDeptNo;
    }
}
