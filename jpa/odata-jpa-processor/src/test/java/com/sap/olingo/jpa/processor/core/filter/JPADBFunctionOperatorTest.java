package com.sap.olingo.jpa.processor.core.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPADataBaseFunction;
import com.sap.olingo.jpa.metadata.core.edm.mapper.api.JPAOperationResultParameter;
import com.sap.olingo.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import com.sap.olingo.jpa.processor.core.exception.ODataJPAProcessorException;
import com.sap.olingo.jpa.processor.core.testmodel.AdministrativeDivision;

class JPADBFunctionOperatorTest {
  private CriteriaBuilder cb;
  private JPADBFunctionOperator cut;
  private UriResourceFunction uriFunction;
  private JPAVisitor jpaVisitor;
  private JPADataBaseFunction jpaFunction;
  private JPAOperationResultParameter jpaResultParam;
  private List<UriParameter> uriParams;

  @BeforeEach
  void setUp() throws Exception {

    cb = mock(CriteriaBuilder.class);
    jpaVisitor = mock(JPAVisitor.class);
    when(jpaVisitor.getCriteriaBuilder()).thenReturn(cb);
    uriFunction = mock(UriResourceFunction.class);
    jpaFunction = mock(JPADataBaseFunction.class);
    jpaResultParam = mock(JPAOperationResultParameter.class);
    when(jpaFunction.getResultParameter()).thenReturn(jpaResultParam);
    final List<UriResource> resources = new ArrayList<>();
    resources.add(uriFunction);

    uriParams = new ArrayList<>();

    cut = new JPADBFunctionOperator(jpaVisitor, uriParams, jpaFunction);
  }

  @SuppressWarnings("unchecked")
  @Test
  void testReturnsExpression() throws ODataApplicationException {

    final Expression<?>[] jpaParameter = new Expression<?>[0];

    when(jpaFunction.getDBName()).thenReturn("Test");
    doReturn(Integer.valueOf(5).getClass()).when(jpaResultParam).getType();
    when(cb.function(jpaFunction.getDBName(), jpaResultParam.getType(), jpaParameter)).thenReturn(mock(
        Expression.class));
    when(jpaFunction.getResultParameter()).thenReturn(jpaResultParam);
    final Expression<?> act = cut.get();
    assertNotNull(act);
  }

  @Test
  void testAbortOnNonFunctionReturnsCollection() {

    when(jpaFunction.getDBName()).thenReturn("org.apache.olingo.jpa::Siblings");
    when(jpaResultParam.isCollection()).thenReturn(true);

    assertThrows(ODataApplicationException.class, () -> cut.get(), "Function provided not checked");
  }

  @Test
  void testAbortOnNonScalarFunction() {

    when(jpaFunction.getDBName()).thenReturn("org.apache.olingo.jpa::Siblings");
    when(jpaResultParam.isCollection()).thenReturn(false);
    doReturn(AdministrativeDivision.class).when(jpaResultParam).getType();

    assertThrows(ODataApplicationException.class, () -> cut.get());

  }

  @Test
  void testAbortOnParameterReadException() throws ODataJPAModelException {

    when(jpaFunction.getDBName()).thenReturn("org.apache.olingo.jpa::Siblings");
    doReturn(Integer.valueOf(5).getClass()).when(jpaResultParam).getType();
    when(jpaFunction.getParameter()).thenThrow(ODataJPAModelException.class);

    final ODataJPAProcessorException act = assertThrows(ODataJPAProcessorException.class, () -> cut.get());
    assertEquals(500, act.getStatusCode());
  }

  @Test
  void testReturnsName() {
    when(jpaFunction.getDBName()).thenReturn("org.apache.olingo.jpa::Siblings");
    assertEquals("org.apache.olingo.jpa::Siblings", cut.getName());
  }
}
