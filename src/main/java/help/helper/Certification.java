package help.helper;

public class Certification {
	
	private String name;
	private Integer hours;
	private String description;
	
	
	@Override
	public String toString() {
		return "Certification [name=" + name + ", hours=" + hours + ", description=" + description + "] \n";
	}

	public Certification(String name, Integer hours, String description) {
		super();
		this.name = name;
		this.hours = hours;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
