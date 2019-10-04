package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.WeatherRepository;
import top.techial.knowledge.domain.Weather;

/**
 * @author techial
 */
@Service
public class WeatherServer {
    private final WeatherRepository weatherRepository;

    public WeatherServer(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Weather findByTitle(String title) {
        return weatherRepository.findByTitle(title).orElseThrow(NullPointerException::new);
    }
}
