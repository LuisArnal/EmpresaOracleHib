package dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement(name="empleados")
public class EmpleadosXML {
    private ArrayList<EmpleadoXML> empleados;

    public EmpleadosXML() {}

    @XmlElement(name="empleado")
    public ArrayList<EmpleadoXML> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(ArrayList<EmpleadoXML> empleados) {
        this.empleados = empleados;
    }
}
