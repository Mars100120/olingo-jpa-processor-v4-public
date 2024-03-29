package com.sap.olingo.jpa.processor.core.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriHelper;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import com.sap.olingo.jpa.processor.core.api.JPAODataSessionContextAccess;
import com.sap.olingo.jpa.processor.core.exception.ODataJPASerializerException;

public abstract class TestJPASerializerCollection {

  protected JPASerializer cut;
  protected ServiceMetadata serviceMetadata;
  protected ODataSerializer serializer;
  protected UriInfo uriInfo;
  protected UriHelper uriHelper;
  protected ODataRequest request;
  protected EntityCollection result;
  protected JPAODataSessionContextAccess context;
  protected Annotatable annotatable;
  protected EdmEntityType edmEt;
  protected EdmEntitySet edmEs;

  public TestJPASerializerCollection() {
    super();
  }

  @BeforeEach
  public void setup() throws SerializerException {
    context = mock(JPAODataSessionContextAccess.class);
    serviceMetadata = mock(ServiceMetadata.class);
    uriInfo = mock(UriInfo.class);
    serializer = mock(ODataSerializer.class);
    uriHelper = mock(UriHelper.class);
    request = mock(ODataRequest.class);
    result = mock(EntityCollection.class);
    edmEt = mock(EdmEntityType.class);
    edmEs = mock(EdmEntitySet.class);

    final UriResourceEntitySet uriEs = mock(UriResourceEntitySet.class);
    final List<UriParameter> keys = Collections.emptyList();

    final List<UriResource> resourceParts = new ArrayList<>();
    resourceParts.add(uriEs);

    when(uriInfo.getUriResourceParts()).thenReturn(resourceParts);
    when(uriEs.getKind()).thenReturn(UriResourceKind.entitySet);
    when(uriEs.getEntitySet()).thenReturn(edmEs);
    when(uriEs.getKeyPredicates()).thenReturn(keys);

    when(uriHelper.buildContextURLSelectList(any(), any(), any())).thenReturn("");
    when(edmEs.getEntityType()).thenReturn(edmEt);
    when(edmEt.getName()).thenReturn("Person");
    when(edmEt.getFullQualifiedName()).thenReturn(new FullQualifiedName("test.Person"));

    initTest(resourceParts);
  }

  @Test
  public void testAnnotatableContextUrlFilledForAbsoluteRequested() throws SerializerException,
      ODataJPASerializerException {
    when(context.useAbsoluteContextURL()).thenReturn(true);
    when(request.getRawBaseUri()).thenReturn("localhost:8080/v1/");
    ((JPAOperationSerializer) cut).serialize(annotatable, getType(), request);
    verifySerializerCall(serializer, "localhost:8080/v1/");
  }

  @Test
  public void testAnnotatableContextUrlFilledForAbsoluteRequestedWithOutSlash() throws SerializerException,
      ODataJPASerializerException {
    when(context.useAbsoluteContextURL()).thenReturn(true);
    when(request.getRawBaseUri()).thenReturn("localhost:8080/v1");
    ((JPAOperationSerializer) cut).serialize(annotatable, getType(), request);
    verifySerializerCall(serializer, "localhost:8080/v1/");
  }

  @Test
  public void testAnnotatableContextUrlNullForRelativeRequested() throws SerializerException,
      ODataJPASerializerException {
    when(context.useAbsoluteContextURL()).thenReturn(false);
    ((JPAOperationSerializer) cut).serialize(annotatable, getType(), request);
    verifySerializerCall(serializer, null);
  }

  @Test
  public void testCreateODataSerializerCanBeCreated() {
    assertNotNull(cut);
  }

  @Test
  public void testGetContentTypeReturnsJSON() {
    assertEquals(ContentType.APPLICATION_JSON, cut.getContentType());
  }

  @Test
  public void testRequestSerialize() throws ODataJPASerializerException, SerializerException {
    assertNull(((JPAOperationSerializer) cut).serialize(request, result));
  }

  protected abstract <T> ArgumentMatcher<T> createMatcher(final String pattern);

  protected abstract EdmType getType();

  protected abstract void initTest(final List<UriResource> resourceParts);

  protected abstract <T> void verifySerializerCall(final ODataSerializer serializer, final String pattern)
      throws SerializerException;

}