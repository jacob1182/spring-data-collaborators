package org.jasi.springdata.collaborators;

import org.jasi.springdata.collaborators.domain.Order;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionUtilsTest {

    interface GenericInterface<T> { }
    interface FirstNoise { }
    interface SecondNoise<T> { }
    interface ThirdNoise<T> extends GenericInterface<T> { }
    interface OrderNoise extends GenericInterface<Order> { }

    private class Candidate1 implements GenericInterface<Order> { }
    private class Candidate2 implements FirstNoise, SecondNoise<String>, GenericInterface<Order> { }
    private class Candidate3 implements FirstNoise, SecondNoise<String>, ThirdNoise<Order> { }
    private class Candidate4 implements FirstNoise, OrderNoise { }
    private class Candidate5 extends Candidate3 { }
    private class Candidate6 implements FirstNoise, SecondNoise<Order> { }

    @Test
    public void firstGenericArgumentType() {

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate1.class))
                .as("direct interface")
                .isEqualTo(Order.class);

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate2.class))
                .as("direct interface with noise")
                .isEqualTo(Order.class);

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate3.class))
                .as("indirect interface with noise")
                .isEqualTo(Order.class);

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate4.class))
                .as("indirect interface with fixed parameter and noise")
                .isEqualTo(Order.class);

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate5.class))
                .as("direct inheritance and indirect interface with noise")
                .isEqualTo(Order.class);

        assertThat(ReflectionUtils.firstGenericArgumentType(GenericInterface.class, Candidate6.class))
                .as("no target interface and noise")
                .isNull();
    }
}