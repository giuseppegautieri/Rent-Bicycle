package model.Bike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class AbstractBike {

	private String categoria;
	protected List<String> equipaggiamento;

	public AbstractBike() {
		equipaggiamento = new ArrayList<>();
	}

	public AbstractBike(String categoria) {
		this.categoria = categoria;
		this.equipaggiamento = new ArrayList<>();
	}

	public void addEquipaggiamento(String equipaggiamento) {
		this.equipaggiamento.add(equipaggiamento);
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public List<String> getEquipaggiamento() {
		return equipaggiamento;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Bike: ").append(this.categoria);
		for (String x : this.equipaggiamento)
			s.append(", ").append(x);
		return s.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof AbstractBike)) return false;
		AbstractBike other = (AbstractBike) o;
		return Objects.equals(this.categoria, other.categoria)
				&& Objects.equals(this.equipaggiamento, other.equipaggiamento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoria, equipaggiamento);
	}

}
