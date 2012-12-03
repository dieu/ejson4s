package me.apanasenko.ejson4s;

import java.util.List;
import java.util.Map;

/**
 * @author Anton Panasenko
 */

public class JavaComplex {
    private String name;
    private List<Simple> likes;
    private Map<String, Object> parameters;

    public JavaComplex() {
    }

    public JavaComplex(String name, Map<String, Object> parameters, List<Simple> likes) {
        this.name = name;
        this.parameters = parameters;
        this.likes = likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public List<Simple> getLikes() {
        return likes;
    }

    public void setLikes(List<Simple> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JavaComplex that = (JavaComplex) o;

        if (likes != null ? !likes.equals(that.likes) : that.likes != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (likes != null ? likes.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
