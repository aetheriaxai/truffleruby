/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: EPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v20.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.truffleruby.parser.ast;

import java.util.List;

import com.oracle.truffle.api.strings.TruffleString;
import org.truffleruby.language.SourceIndexLength;
import org.truffleruby.parser.ast.types.INameNode;
import org.truffleruby.parser.ast.visitor.NodeVisitor;

/** Declaration (and assignment) of a Constant. */
public final class ConstDeclParseNode extends AssignableParseNode implements INameNode {
    private final String name;
    private final INameNode constNode;

    // TODO: Split this into two sub-classes so that name and constNode can be specified separately.
    public ConstDeclParseNode(
            SourceIndexLength position,
            TruffleString name,
            INameNode constNode,
            ParseNode valueNode) {
        super(position, valueNode);

        assert constNode != null || (name != null && !name.isEmpty());
        this.name = name == null ? null : name.toJavaStringUncached();
        this.constNode = constNode;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CONSTDECLNODE;
    }

    /** Accept for the visitor pattern.
     * 
     * @param iVisitor the visitor **/
    @Override
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitConstDeclNode(this);
    }

    /** Gets the name (this is the rightmost element of lhs (in Foo::BAR it is BAR).
     * 
     * @return name the constant Name, it normally starts with a Capital */
    public String getName() {
        return (name == null ? constNode.getName() : name);
    }

    /** Get the full path, including the name of the new constant (in Foo::BAR it is Foo::BAR) or null. Your probably
     * want to extract the left part with <code>((Colon2ParseNode) node.getConstNode()).getLeftNode()</code> if
     * <code>node.getConstNode()</code> is a <code>Colon2ConstParseNode</code>.
     * 
     * @return pathNode */
    public ParseNode getConstNode() {
        return (ParseNode) constNode;
    }

    @Override
    public List<ParseNode> childNodes() {
        return createList(getConstNode(), getValueNode());
    }

    @Override
    public boolean needsDefinitionCheck() {
        return false;
    }
}
