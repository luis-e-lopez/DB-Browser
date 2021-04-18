$(function () {
  // API Endpoint calls

  function getDatabases(connectionId, callback, errorCallback) {
    $.ajax({
      url: "/schemas?connectionId=" + connectionId,
      type: "GET",
      dataType: "json",
      success: function (data) {
        callback(data["schemas"], connectionId);
      },
      error: function (xhr) {
        errorCallback(xhr, connectionId);
      },
    });
  }

  function getTables(connectionId, schema, callback, errorCallback) {
    $.ajax({
      url: "/tables?connectionId=" + connectionId + "&schema=" + schema,
      type: "GET",
      dataType: "json",
      success: function (data) {
        callback(data.tables, schema, connectionId);
      },
      error: function (xhr) {
        errorCallback(xhr, connectionId);
      },
    });
  }

  function getColumns(connectionId, schema, table, callback, errorCallback) {
    $.ajax({
      url:
        "/columns?connectionId=" +
        connectionId +
        "&schema=" +
        schema +
        "&table=" +
        table,
      type: "GET",
      dataType: "json",
      success: function (data) {
        callback(data.columns, table, schema, connectionId);
      },
      error: function (xhr) {
        errorCallback(xhr, connectionId);
      },
    });
  }

  function getPreviewTable(
    connectionId,
    schema,
    table,
    callback,
    errorCallback
  ) {
    $.ajax({
      url:
        "/tablePreview?connectionId=" +
        connectionId +
        "&schema=" +
        schema +
        "&table=" +
        table,
      type: "GET",
      dataType: "json",
      success: function (data) {
        callback(data.rows, data.columns);
      },
      error: function (xhr) {
        errorCallback(xhr, connectionId);
      },
    });
  }

  function getStatisticsTable(
      connectionId,
      schema,
      table,
      callback,
      errorCallback
    ) {
      $.ajax({
        url:
          "/tableStats?connectionId=" +
          connectionId +
          "&schema=" +
          schema +
          "&table=" +
          table,
        type: "GET",
        dataType: "json",
        success: function (data) {
          callback(data.stats, schema, table);
        },
        error: function (xhr) {
          errorCallback(xhr, connectionId);
        },
      });
    }

   function getStatisticsColumn(
     connectionId,
     schema,
     table,
     column,
     callback,
     errorCallback
   ) {
     $.ajax({
       url:
         "/columnStats?connectionId=" +
         connectionId +
         "&schema=" +
         schema +
         "&table=" +
         table +
         "&column=" +
         column,
       type: "GET",
       dataType: "json",
       success: function (data) {
         callback(data.stats, schema, table, column);
       },
       error: function (xhr) {
         errorCallback(xhr, connectionId);
       },
     });
   }

  //////////
  // DOM manipulation and event handlers
  /////////

  init();

  function init() {
    initClickHandlers();
    hideErrorMessageCallouts();
  }

  function hideErrorMessageCallouts() {
    $(".error-message").hide();
  }

  function initClickHandlers() {
    $(".schemas-level").click(function () {
      let expand = $(this).attr("aria-expanded");
      if (expand === "true") {
        let connectionId = $(this).data("connectionid");
        getDatabases(connectionId, populateDatabasesList, showError);
      }
    });
  }

  function addTablesClickHandler(element) {
    $(element)
      .find(".tables-level")
      .click(function (e) {
        let expand = $(this).attr("aria-expanded");
        if (expand === "false") {
          let connectionId = $(this)
            .closest(".schemas-level")
            .data("connectionid");
          let schema = $(this).closest(".db-menu").data("schema");
          getTables(connectionId, schema, populateTablesList, showError);
        }
        e.stopPropagation();
      });
  }

  function addColumnsClickHandler(element) {
    $(element)
      .find(".columns-level")
      .click(function (e) {
        let expand = $(this).attr("aria-expanded");
        if (expand === "false") {
          let connectionId = $(this)
            .closest(".schemas-level")
            .data("connectionid");
          let schema = $(this).closest(".db-menu").data("schema");
          let table = $(this).closest(".table-menu").data("table");
          getColumns(
            connectionId,
            schema,
            table,
            populateColumnsList,
            showError
          );
        }
        e.stopPropagation();
      });
  }

  function addPreviewTableClickHandler(element) {
    $(element)
      .find(".preview-table")
      .click(function (e) {
        let connectionId = $(this)
          .closest(".schemas-level")
          .data("connectionid");
        let schema = $(this).closest(".db-menu").data("schema");
        let table = $(this).data("preview-table");
        getPreviewTable(
          connectionId,
          schema,
          table,
          populatePreviewTable,
          showError
        );
        populatePreviewTableInfo(schema, table);
        e.stopPropagation();
      });
  }

  function addStatisticsTableClickHandler(element) {
      $(element)
        .find(".stats-table")
        .click(function (e) {
          let connectionId = $(this)
            .closest(".schemas-level")
            .data("connectionid");
          let schema = $(this).closest(".db-menu").data("schema");
          let table = $(this).data("stats-table");
          getStatisticsTable(
            connectionId,
            schema,
            table,
            populateStatisticsTableInfo,
            showError
          );
          e.stopPropagation();
        });
    }

  function addStatisticsColumnClickHandler(element) {
      $(element)
        .find(".column-menu")
        .click(function (e) {
            let connectionId = $(this)
              .closest(".schemas-level")
              .data("connectionid");
            let schema = $(this).closest(".db-menu").data("schema");
            let table = $(this).closest(".table-menu").data("table");
            let column = $(this).data("column");

            getStatisticsColumn(
              connectionId,
              schema,
              table,
              column,
              populateStatisticsColumnInfo,
              showError
            );

            e.stopPropagation();
        });
    }

  function populateDatabasesList(databases, connectionId) {
    let schemaElement = $(".schemas-level").find(
      `[data-connectionid='${connectionId}']`
    );
    schemaElement.empty();
    databases.forEach(function (db) {
      schemaElement.append(createAccordionHTMLForDatabases(db.name));
    });
    initDBMenu(schemaElement);
  }

  function createAccordionHTMLForDatabases(dbName) {
    var db =
      '<li class="db-menu" data-schema="' +
      dbName +
      '" aria-expanded="false"><a class="subitem" >' +
      dbName +
      "</a></li>";
    return db;
  }

  function createAccordionHTMLForTables(tableName) {
    var previewButton =
      '<button type="button" data-preview-table=' +
      tableName +
      ' class="hollow button preview-table">prev</button>';
    var statisticsButton =
      '<button type="button" data-stats-table=' +
      tableName +
      ' class="hollow button stats-table">stats</button>';
    var table =
      '<li class="table-menu" data-table="' +
      tableName +
      '" aria-expanded="false"><a class="subitem" >' +
      tableName +
      previewButton +
      statisticsButton +
      "</a></li>";
    return table;
  }

  function createAccordionHTMLForColumns(columnName, columnType, columnKey) {
    var column =
      '<li class="column-menu" data-column="' +
      columnName +
      '" aria-expanded="false"><a class="subitem" >' +
      "<b>" +
      columnName +
      "</b>" +
      " (" +
      columnType +
      ") " +
      columnKey +
      "</a></li>";
    return column;
  }

  function initDBMenu(schemaElement) {
    $(schemaElement)
      .find(".db-menu")
      .click(function (e) {
        let expand = $(this).attr("aria-expanded");
        if (expand === "true") {
          $(this).find("ul").remove();
          $(this).attr("aria-expanded", "false");
        } else {
          $(this).append(
            '<ul class="menu vertical sublevel-2"><li class="tables-level" aria-expanded="false"><a class="subitem" >Tables</a><ul class="menu vertical sublevel-3 tables-sublevel"></ul></li></ul>'
          );
          $(this).attr("aria-expanded", "true");
          addTablesClickHandler($(this));
        }
        e.stopPropagation();
      });
  }

  function initTablesMenu(tablesElement) {
    $(tablesElement)
      .find(".table-menu")
      .click(function (e) {
        let expand = $(this).attr("aria-expanded");
        if (expand === "true") {
          $(this).find("ul").remove();
          $(this).attr("aria-expanded", "false");
        } else {
          $(this).append(
            '<ul class="menu vertical sublevel-4"><li class="columns-level" aria-expanded="false"><a class="subitem" >Columns</a><ul class="menu vertical sublevel-5 columns-sublevel"></ul></li></ul>'
          );
          $(this).attr("aria-expanded", "true");
          addColumnsClickHandler($(this));
        }
        e.stopPropagation();
      });
  }

  function populateTablesList(tables, schema, connectionId) {
    let tablesElement = $(".schemas-level")
      .find(`[data-connectionid='${connectionId}']`)
      .find(`[data-schema='${schema}']`)
      .find(".tables-sublevel");
    tablesElement.empty();
    tables.forEach(function (table) {
      tablesElement.append(createAccordionHTMLForTables(table.name));
    });
    initTablesMenu(tablesElement);
    addPreviewTableClickHandler(tablesElement);
    addStatisticsTableClickHandler(tablesElement);
  }

  function populateColumnsList(columns, table, schema, connectionId) {
    let columnsElement = $(".schemas-level")
      .find(`[data-connectionid='${connectionId}']`)
      .find(`[data-schema='${schema}']`)
      .find(`[data-table='${table}']`)
      .find(".columns-sublevel");
    columnsElement.empty();
    columns.forEach(function (column) {
      columnsElement.append(
        createAccordionHTMLForColumns(
          column.name,
          column.type,
          column.key
        )
      );
    });
    addStatisticsColumnClickHandler(columnsElement);
  }

  function populatePreviewTable(rows, columns) {
    let previewTableElement = $(".table-scroll");
    previewTableElement.empty();
    let columnNames = "";
    columns.forEach(function (column) {
      columnNames += "<th>" + column.name + "</th>";
    });
    let rowsData = "";
    rows.forEach(function (row) {
      let currentRow = "";
      columns.forEach(function (column) {
        currentRow += "<td>" + row[column.name] + "</td>";
      });
      rowsData += "<tr>" + currentRow + "</tr>";
    });
    let tableHead = "<thead><tr>" + columnNames + "</tr></thead>";
    let tableBody = "<tbody>" + rowsData + "</tbody>";
    let table = "<table>" + tableHead + tableBody + "</table>";
    previewTableElement.append(table);
  }

  function populatePreviewTableInfo(schema, table) {
    $(".preview-table-info").empty();
    $(".preview-table-info").append(
      "<h4>Preview of table: <b>" +
        table +
        "</b> (max 10 rows)</h4>" +
        "<h5>Database: " +
        schema +
        "</h5>"
    );
  }

  function populateStatisticsTableInfo(stats, schema, table) {
      $(".statistics-table-info").empty();
      $(".statistics-table-info").append(
        "<h4>Statistics of table: <b>" +
          table +
          "</b> </h4>" +
          "<h5>Database: " +
          schema +
          "</h5>" +
          "<p>Number of Attributes: <b>" + stats.columnsCount + "</b>, " +
          "Number of Records: <b>" + stats.recordsCount + "</b></p><hr/>"
      );
    }

  function populateStatisticsColumnInfo(stats, schema, table, column) {
    $(".statistics-column-info").empty();
    $(".statistics-column-info").append(
      "<h4>Statistics of column: <b>" +
        column +
        "</b> </h4>" +
        "<h5>Database: " +
        schema +
        "</h5>" +
        "<h5>Table: " +
        table +
        "</h5>" +
        "<p>MAX: <b>" + stats.max + "</b>, " +
        "MIN: <b>" + stats.min + "</b>, " +
        "AVG: <b>" + stats.avg + "</b></p><hr/>"
    );
  }

  function showError(error, connectionId) {
    let errorMessageElement = $(
      `.error-message[data-error-message-connectionid='${connectionId}']`
    );
    $(errorMessageElement).empty();
    $(errorMessageElement).append(error.responseJSON.message);
    $(errorMessageElement).fadeIn(500).delay(5000).fadeOut(500);
  }
});
