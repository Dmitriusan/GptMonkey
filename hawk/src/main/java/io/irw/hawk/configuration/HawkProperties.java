package io.irw.hawk.configuration;

import io.irw.hawk.dto.merchandise.ProductVariantEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "hawk")
@Data
public class HawkProperties {

    private Ai ai;
    private Sampling sampling;
    private Subset subset;

    public boolean someAiFeaturesAreDisabled() {
        return !ai.isEnabled()
                || !ai.getFeatures().isPieceCountMatching();
    }

    public boolean samplingIsEnabled() {
        return sampling.isEnabled();
    }

    public boolean subsetIsEnabled() {
        return subset.isEnabled();
    }

    @Data
    public static class Ai {
        private boolean enabled;
        private Features features;

        @Data
        public static class Features {
            private boolean pieceCountMatching;
        }
    }

    @Data
    public static class Sampling {
        private boolean enabled;
        private double rate;
    }

    @Data
    public static class Subset {
        private boolean enabled;
        private Products products;
    }

    @Data
    public static class Products {
        private List<ProductVariantEnum> variants;
    }

}
