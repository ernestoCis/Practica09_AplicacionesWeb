package cisneros.jesus.GamesStop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Juego{
    private Long id;
    private String titulo;
    private String descripcion;

    // Almacenaremos el nombre del archivo o la URL de la imagen
    private String portadaURL;

    public String getPortadaUrl() {
        return portadaURL;
    }

    public void setPortadaUrl(String portadaUrl) {
        this.portadaURL = portadaUrl;
    }
}