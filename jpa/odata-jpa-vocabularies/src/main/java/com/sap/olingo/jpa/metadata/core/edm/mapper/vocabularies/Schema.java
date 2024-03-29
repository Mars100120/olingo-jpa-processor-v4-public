package com.sap.olingo.jpa.metadata.core.edm.mapper.vocabularies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlComplexType;
import org.apache.olingo.commons.api.edm.provider.CsdlEnumType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.edm.provider.CsdlTerm;
import org.apache.olingo.commons.api.edm.provider.CsdlTypeDefinition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * http://docs.oasis-open.org/odata/ns/edmx
 * @author Oliver Grande
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class Schema {
  @JacksonXmlProperty(isAttribute = true)
  private String xmlns;

  @JacksonXmlProperty(isAttribute = true, localName = "Namespace")
  private String namespace;

  @JacksonXmlProperty(isAttribute = true, localName = "Alias")
  private String alias;

  @JacksonXmlProperty(localName = "EnumType")
  private final List<EnumType> enumerations = new ArrayList<>();

  @JacksonXmlProperty(localName = "TypeDefinition")
  private final List<TypeDefinition> typeDefinitions = new ArrayList<>();

  @JacksonXmlProperty(localName = "ComplexType")
  private final List<ComplexType> complexTypes = new ArrayList<>();

  @JacksonXmlProperty(localName = "Term")
  private final List<Term> terms = new ArrayList<>();

  @JacksonXmlProperty(localName = "Function")
  private final List<Function> functions = new ArrayList<>();

  @JacksonXmlProperty(localName = "Action")
  private final List<Action> actions = new ArrayList<>();

  CsdlSchema asCsdlSchema() {
    final CsdlSchema csdlSchema = new CsdlSchema();
    csdlSchema.setAlias(alias);
    csdlSchema.setNamespace(namespace);
    csdlSchema.setTerms(asCsdlTerms());
    csdlSchema.setEnumTypes(asEnumTypes());
    csdlSchema.setComplexTypes(asComplexTypes());
    csdlSchema.setTypeDefinitions(asTypeDefinitions());
    csdlSchema.setFunctions(asFunctions());
    csdlSchema.setActions(asActions());
    return csdlSchema;
  }

  String getNamespace() {
    return namespace;
  }

  List<Term> getTerms() {
    return Collections.unmodifiableList(terms);
  }

  void setAlias(final String alias) {
    this.alias = alias;
  }

  @JsonSetter
  void setComplexTypes(final ComplexType[] newComplexTypes) {
    complexTypes.addAll(Arrays.asList(newComplexTypes));
  }

  @JsonSetter
  void setEnumerations(final EnumType enumeration) {
    this.enumerations.add(enumeration);
  }

  @JsonSetter
  void setFunctions(final Function[] newFunctions) {
    functions.addAll(Arrays.asList(newFunctions));
  }

  void setNamespace(final String namespace) {
    this.namespace = namespace;
  }

  @JsonSetter
  void setTerms(final Term[] newTerms) {
    terms.addAll(Arrays.asList(newTerms));
  }

  @JsonSetter
  void setTypeDefinitions(final TypeDefinition typeDefinition) {
    this.typeDefinitions.add(typeDefinition);
  }

  private List<CsdlAction> asActions() {
    return Collections.unmodifiableList(actions);
  }

  private List<CsdlComplexType> asComplexTypes() {
    return Collections.unmodifiableList(complexTypes);
  }

  private List<CsdlTerm> asCsdlTerms() {
    return Collections.unmodifiableList(terms);
  }

  private List<CsdlEnumType> asEnumTypes() {
    return Collections.unmodifiableList(enumerations);
  }

  private List<CsdlFunction> asFunctions() {
    return Collections.unmodifiableList(functions);
  }

  private List<CsdlTypeDefinition> asTypeDefinitions() {
    return Collections.unmodifiableList(typeDefinitions);
  }
}
