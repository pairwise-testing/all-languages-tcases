//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2012, Cornutum Project
//                             www.cornutum.org
//                           All Rights Reserved
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.tcases;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.Test;
import static org.cornutum.hamcrest.Composites.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Runs tests for {@link VarDefIterator}.
 *
 */
public class TestVarDefIterator
  {
  /**
   * Test when traversing a list of various {@link IVarDef} types.
   */
  @Test
  public void testListTraversal()
    {
    // Given...
    FunctionInputDef inputDef = new FunctionInputDef( "testListTraversal");
    VarSet varSet;

    inputDef.addVarDef( new VarDef( "1"));

    varSet = new VarSet( "2");
    varSet.addMember( new VarDef( "1"));
    varSet.addMember( new VarDef( "2"));
    varSet.addMember( new VarDef( "3"));
    inputDef.addVarDef( varSet);

    varSet = new VarSet( "empty-1");
    inputDef.addVarDef( varSet);

    varSet = new VarSet( "empty-2");
    inputDef.addVarDef( varSet);

    inputDef.addVarDef( new VarDef( "3"));

    // When...
    String[] varDefNames =
      IteratorUtils.toArray(
        IteratorUtils.transformedIterator(
          new VarDefIterator( inputDef),
          VarDef::getPathName),
        String.class);
    
    // Then...
    assertThat
      ( "VarDef sequence",
        varDefNames,
        listsElements( "1", "2.1", "2.2", "2.3", "3"));
    }
  /**
   * Test when traversing a tree of  {@link VarSet variable sets}.
   */
  @Test
  public void testTreeTraversal()
    {
    // Given...
    FunctionInputDef inputDef = new FunctionInputDef( "testTreeTraversal");
    VarSet varSet;
    VarSet member;

    varSet = new VarSet( "1");
    varSet.addMember( new VarSet( "1"));
    varSet.addMember( new VarSet( "2"));
    inputDef.addVarDef( varSet);

    member = (VarSet) varSet.getMember( "1");
    member.addMember( new VarSet( "1"));
    member.addMember( new VarDef( "2"));

    member = (VarSet) member.getMember( "1");
    member.addMember( new VarDef( "1"));
    member.addMember( new VarDef( "2"));
    
    member = (VarSet) varSet.getMember( "2");
    member.addMember( new VarDef( "1"));
    
    varSet = new VarSet( "2");
    varSet.addMember( new VarDef( "1"));
    varSet.addMember( new VarSet( "2"));
    inputDef.addVarDef( varSet);
    
    member = (VarSet) varSet.getMember( "2");
    member.addMember( new VarDef( "1"));
    member.addMember( new VarDef( "2"));

    // When...
    String[] varDefNames =
      IteratorUtils.toArray(
        IteratorUtils.transformedIterator(
          new VarDefIterator( inputDef),
          VarDef::getPathName),
        String.class);
    
    // Then...
    assertThat
      ( "VarDef sequence",
        varDefNames,
        listsElements( "1.1.1.1", "1.1.1.2", "1.1.2", "1.2.1", "2.1", "2.2.1", "2.2.2"));
    }
  }

