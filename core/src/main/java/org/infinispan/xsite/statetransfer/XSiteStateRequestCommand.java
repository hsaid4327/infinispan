/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other
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

package org.infinispan.xsite.statetransfer;

import org.infinispan.CacheException;
import org.infinispan.commands.ReplicableCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.remoting.responses.SuccessfulResponse;
import org.infinispan.remoting.transport.Address;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 *
 */
public class XSiteStateRequestCommand implements ReplicableCommand {

    private static final Log log = LogFactory.getLog(XSiteStateRequestCommand.class);

    public enum Type {
        START_XSITE_STATE_TRANSFER


    }
    //TODO Not sure about this one
    public static final byte COMMAND_ID = 15;

    private Type type;
    private String cacheName;
    private String destinationSiteName;
    private Address origin;
    private XSiteStateProvider xSiteStateProvider;
    private String sourceSiteName;


    public XSiteStateRequestCommand(String destinationSiteName, String sourceSiteName, String cacheName, Address address, Type type) {
        this.destinationSiteName = destinationSiteName;
        this.cacheName = cacheName;
        this.origin = address;
        this.type = type;
        this.sourceSiteName = sourceSiteName;
    }

    @Inject
    public void init(XSiteStateProvider xSiteStateProvider) {
        this.xSiteStateProvider = xSiteStateProvider;
    }

    @Override
    public Object perform(InvocationContext ctx) throws Throwable {
        final boolean trace = log.isTraceEnabled();
        LogFactory.pushNDC(cacheName, trace);
        try {
            switch (type) {
                case START_XSITE_STATE_TRANSFER:
                    Object responseValue = xSiteStateProvider.startXSiteStateTransfer(destinationSiteName, sourceSiteName, cacheName, origin);
                    return SuccessfulResponse.create(responseValue);

                default:
                    throw new CacheException("Unknown state request command type: " + type);
            }
        } finally {
            LogFactory.popNDC(trace);
        }
    }

    public Address getOrigin() {
        return origin;
    }

    public void setOrigin(Address origin) {
        this.origin = origin;
    }

    @Override
    public byte getCommandId() {
        return COMMAND_ID;
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{(byte) type.ordinal(), getOrigin(), cacheName, destinationSiteName};
    }


    @Override
    @SuppressWarnings("unchecked")
    public void setParameters(int commandId, Object[] parameters) {
        int i = 0;
        type = Type.values()[(Byte) parameters[i++]];
        setOrigin((Address) parameters[i++]);
        cacheName = (String) parameters[i++];
        destinationSiteName = (String) parameters[i++];

    }

    @Override
    public boolean isReturnValueExpected() {
        return false;
    }

    @Override
    public String toString() {
        return "XSiteStateRequestCommand{" +
                "type=" + type +
                ", cacheName='" + cacheName + '\'' +
                ", destinationSiteName='" + destinationSiteName + '\'' +
                ", origin=" + origin +
                ", sourceSiteName='" + sourceSiteName + '\'' +
                '}';
    }
}
