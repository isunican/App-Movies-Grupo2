package es.unican.movies.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterSeries {

    public String title;
    public List<String> genres;

}
