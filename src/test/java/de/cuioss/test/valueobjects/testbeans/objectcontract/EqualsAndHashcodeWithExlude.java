package de.cuioss.test.valueobjects.testbeans.objectcontract;

import de.cuioss.test.valueobjects.api.object.ObjectTestConfig;
import lombok.Data;

/**
 * @author Oliver Wolff
 */
@Data
@ObjectTestConfig(equalsAndHashCodeExclude = "excluded")
public class EqualsAndHashcodeWithExlude {

    private String name;
    private String excluded;
    private boolean buggar;
    private Integer number;
    private Boolean state;

}
