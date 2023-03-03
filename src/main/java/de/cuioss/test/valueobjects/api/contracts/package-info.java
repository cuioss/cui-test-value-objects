/**
 * Defines concrete TestContracts that are not {@link java.lang.Object} test contracts, which are
 * implicit if not vetoed using
 * {@link de.cuioss.test.valueobjects.api.object.VetoObjectTestContract}
 * Currently defined contracts are:
 * <ul>
 * <li>{@link de.cuioss.test.valueobjects.api.contracts.VerifyBeanProperty}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.contracts.VerifyBuilder}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.contracts.VerifyConstructor}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.contracts.VerifyCopyConstructor}</li>
 * <li>{@link de.cuioss.test.valueobjects.api.contracts.VerifyFactoryMethod}</li>
 * </ul>
 *
 * @author Oliver Wolff
 */
package de.cuioss.test.valueobjects.api.contracts;
