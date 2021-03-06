/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.lucene.impl;

import org.infinispan.lucene.logging.Log;
import org.infinispan.util.logging.LogFactory;


/**
 * Since Lucene requires extension of Directory (it's not an interface)
 * we need to apply some tricks to provide the correct Directory implementation
 * depending on the Lucene version detected on the classpath.
 *
 * @since 5.2
 * @author Sanne Grinovero
 */
public class LuceneVersionDetector {

   public static final int VERSION = detectVersion();

   private static int detectVersion() {
      Log log = LogFactory.getLog(LuceneVersionDetector.class, Log.class);
      int version = 3;
      try {
         Class.forName("org.apache.lucene.store.IOContext", true, LuceneVersionDetector.class.getClassLoader());
         version = 4;
      } catch (ClassNotFoundException e) {
      }
      log.detectedLuceneVersion(version);
      return version;
   }

}
