/*
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
package de.cuioss.test.valueobjects.objects.impl;

import lombok.experimental.UtilityClass;

/**
 * Aggregates constants shared between the different instantiator
 * implementations within this package.
 *
 * @author Oliver Wolff
 */
@UtilityClass
final class InstantiatorConstants {

    /** "Properties must not be null, but may be empty". */
    static final String PROPERTIES_MUST_NOT_BE_NULL = "Properties must not be null, but may be empty";
}
