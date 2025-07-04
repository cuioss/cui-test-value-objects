/**
 * Copyright © 2025 CUI-OpenSource-Software (info@cuioss.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
