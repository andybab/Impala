// Copyright (c) 2012 Cloudera, Inc. All rights reserved.

package com.cloudera.impala.testutil;

import com.cloudera.impala.thrift.TQueryOptions;
import com.google.common.base.Objects;

/*
 * Describes execution details for a query. It contains TQueryOptions for specifying the
 * backend execution and some client side options (such as fetch size).
 * TODO: replace it with TQueryOptions
 */
public class TestExecContext {
  private final TQueryOptions queryOptions;

  //TODO: (lennik) Consider updating this to support different fetch sizes
  private int fetchSize = 1;

  public TestExecContext(int numNodes, int batchSize, boolean disableCodegen,
                         boolean abortOnError, int maxErrors, long maxScanRangeLength,
                         boolean partitionAgg, boolean allowUnsupportedFormats) {
    queryOptions = new TQueryOptions();
    queryOptions.abort_on_error = abortOnError;
    queryOptions.max_errors = maxErrors;
    queryOptions.disable_codegen = disableCodegen;
    queryOptions.batch_size = batchSize;
    queryOptions.num_nodes = numNodes;
    queryOptions.max_scan_range_length = maxScanRangeLength;
    // TODO: turn on multiple threads by setting that 1 to 0.  This doesn't currently
    // pass all the tests due to numerical precision issues.  With multiple threads
    // and a small batch size, aggregation over float columns result in slightly
    // different results.
    queryOptions.num_scanner_threads = 1;
    queryOptions.max_io_buffers = 0;
    queryOptions.allow_unsupported_formats = allowUnsupportedFormats;
    queryOptions.default_order_by_limit = -1;
  }

  public TestExecContext(int numNodes, int batchSize, boolean disableCodegen,
      boolean abortOnError, int maxErrors) {
    this(numNodes, batchSize, disableCodegen, abortOnError, maxErrors, 0, false, true);
  }

  public TestExecContext(TQueryOptions queryOptions, int fetchSize) {
    this.queryOptions = queryOptions.deepCopy();
    this.fetchSize = fetchSize;
  }

  public TQueryOptions getTQueryOptions() {
    return queryOptions;
  }

  public int getFetchSize() {
    return fetchSize;
  }

  @Override
  public String toString() {
      return Objects.toStringHelper(this).add("TQueryOptions", queryOptions.toString())
                                         .add("FetchSize", fetchSize)
                                         .toString();
  }
}
