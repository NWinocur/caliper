package com.nicolaswinocur.json;

import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Objects;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GsonUtilsTest {

  private File outputTestFile;
  private File inputTestFile;
  private OutputStream oStreamToWriteTo;
  private InputStream inStreamToReadFrom;
  private ImmutableSet sampleImmutableSet;
  private ImmutableList sampleImmutableList;
  private ImmutableMap sampleImmutableMap;
  private Collection sampleCollectionOfCustomObjects;

  /** A simple dumb immutable inner class for the purpose of testing/demonstrating serialization and
   *  deserialization of custom classes */
  class myTestingInnerClass implements GsonSerializable {
    public final int myInt;
    public final String myString;
    myTestingInnerClass(int intToSet, String stringToSet){
      this.myInt = intToSet;
      this.myString = stringToSet;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      myTestingInnerClass that = (myTestingInnerClass) o;
      return myInt == that.myInt &&
          Objects.equals(myString, that.myString);
    }

    @Override
    public int hashCode() {
      return Objects.hash(myInt, myString);
    }
  }


  @Before
  public void setUp() throws Exception {
    outputTestFile = new File("outputTestFile.json");
    inputTestFile = new File("inputTestFile.json");
    sampleImmutableSet = ImmutableSet.of("First item", "Second item", "Another item", "Yet another item");
    sampleImmutableList = ImmutableList.of("First item", "Non-unique item", "Non-unique item", "Fourth item", "Last item");
    sampleCollectionOfCustomObjects = ImmutableList.of(new myTestingInnerClass(1, "One"), new myTestingInnerClass(9001, "over nine thousand"));


    oStreamToWriteTo = new FileOutputStream(outputTestFile);
    if (!inputTestFile.exists()){
      Assert.fail("Test as currently written expects an input file to test reading from file");
    }
    else{
      inStreamToReadFrom = new FileInputStream(inputTestFile);
    }
  }

  @After
  public void tearDown() {
    outputTestFile.delete();
  }

  @Test
  public void provideGson() {
    Gson toExamine = GsonUtils.provideGson();
    assertNotNull(toExamine);
  }

  @Test
  public void writeJsonStream() throws IOException {
    GsonUtils.writeJsonStream(oStreamToWriteTo, sampleImmutableSet);
    assertThat(outputTestFile).isNotNull();

    BufferedReader br = new BufferedReader(new FileReader(outputTestFile));
    if (null == br.readLine()){
      Assert.fail("Doesn't appear as though contents got written to test output file");
    }
  }

  @Test
  public void readJsonFrom() throws IOException {
    GsonUtils.writeJsonStream(oStreamToWriteTo, sampleCollectionOfCustomObjects);

    Collection gottenFromFile = GsonUtils.readJsonFrom(inStreamToReadFrom, myTestingInnerClass.class);
    assertThat(gottenFromFile).containsAllIn(sampleCollectionOfCustomObjects);
    assertThat(gottenFromFile).isEqualTo(sampleCollectionOfCustomObjects);
  }
}