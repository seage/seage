/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.grammar;

import org.seage.data.DataNode;
import java.io.Serializable;


/**
 *
 * @author jenik
 */
public interface Symbol extends Serializable
{
    
    public enum Type { TERMINAL, NONTERMINAL };    

    /** @brief evaluate derivate tree
     *  @param symbolTable table of symbols and their values
     *  @retval result of the operation(s)
     */
    abstract public Object eval(DataNode symbolTable);

    /** @brief optimize derivate tree, return new derivate (sub)tree */
    abstract public Symbol optimize();
        
    /** @brief gets type of this symbol (termina, nonterminal) */
    abstract public Type getType();
        
    /** @brief copy ourself, and return new instance */
    abstract public Symbol copy();

}
