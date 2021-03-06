/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package org.infinispan.configuration;

import static org.infinispan.util.Immutables.immutableTypedProperties;

import org.infinispan.util.TypedProperties;

public abstract class AbstractTypedPropertiesConfiguration {

   private final TypedProperties properties;
   
   protected AbstractTypedPropertiesConfiguration(TypedProperties properties) {
      this.properties = immutableTypedProperties(properties);
   }

   public TypedProperties properties() {
      return properties;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      AbstractTypedPropertiesConfiguration that = (AbstractTypedPropertiesConfiguration) o;

      if (properties != null ? !properties.equals(that.properties) : that.properties != null)
         return false;

      return true;
   }

   @Override
   public int hashCode() {
      return properties != null ? properties.hashCode() : 0;
   }

}