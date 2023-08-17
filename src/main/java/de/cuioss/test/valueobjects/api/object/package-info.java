/**
 * Provides structures and classes for {@link java.lang.Object}-contract
 * specific tests. This tests will implicitly be run by
 * {@link de.cuioss.test.valueobjects.ValueObjectTest}. If you want to skip a
 * certain test you can use
 * {@link de.cuioss.test.valueobjects.api.object.VetoObjectTestContract} to do
 * so: {@code @VetoObjectTestContract(ObjectTestContracts#SERIALIZABLE)} will
 * result in the
 * {@link de.cuioss.test.valueobjects.contract.SerializableContractImpl} not to
 * being run. The default contract tests are defined within
 * {@link de.cuioss.test.valueobjects.api.object.ObjectTestContracts}
 * <ul>
 * <li>{@link de.cuioss.test.valueobjects.api.object.ObjectTestContracts#EQUALS_AND_HASHCODE}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.object.ObjectTestContracts#SERIALIZABLE}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.object.ObjectTestContracts#TO_STRING}</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.test.valueobjects.api.object;
