/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Defines concrete TestContracts that are not {@link java.lang.Object} test
 * contracts, which are implicit if not vetoed using
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
