/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jasi.springdata.collaborators;


/**
 * Entity factory to reconstitute entities given a row element.
 *
 * Derived classes will receive a raw element, and should return
 * a reconstituted entity in a consistent state to the domain logic.
 *
 * @author @jacob1182
 */
public interface EntityFactory<T> {

    T reconstitute(T element);
}
