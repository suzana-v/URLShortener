<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Statistics</title>

    <!-- Load React. -->
    <!-- Note: when deploying, replace "development.js" with "production.min.js". -->
    <script src="https://unpkg.com/react@16/umd/react.production.min.js" crossorigin></script>
    <script src="https://unpkg.com/react-dom@16/umd/react-dom.production.min.js" crossorigin></script>
    <script src="https://unpkg.com/babel-standalone@6/babel.min.js"></script>

    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

</head>
<body>

<div id="container"></div>


<!-- Load our React component. -->
<script type="text/babel" >

    var statistics = [
        <#list statisticsMap?keys as key>
            {url: '${key}', redirectCount: '${statisticsMap[key]}'}<#if key_has_next>,</#if>
        </#list>
    ]

    class StatisticRow extends React.Component {
        render() {
            const statistic = this.props.statistic;
            return (
                    <tr>
                        <td>{statistic.url}</td>
                        <td>{statistic.redirectCount}</td>
                    </tr>
            );
        }
    }

    class StatisticsTable extends React.Component {

        render() {
            const rows = [];

            this.props.statistics.forEach((statistic) => {
                rows.push(
                        <StatisticRow
                                statistic={statistic}
                                key={statistic.url} />
                );
            });

            return (
                    <table className="table">
                        <thead>
                        <tr>
                            <th>URL</th>
                            <th>Number of redirects</th>
                        </tr>
                        </thead>
                        <tbody>{rows}</tbody>
                    </table>
            );
        }
    }

    ReactDOM.render(
            <StatisticsTable statistics={statistics} />,
            document.getElementById('container')
    );

</script>

</body>
</html>