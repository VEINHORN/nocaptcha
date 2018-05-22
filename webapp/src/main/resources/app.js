const wSocket = new WebSocket("ws://10.57.31.112:5557/ws");

var captchas = ["1", "2"];

wSocket.onmessage = function (event) {
	console.log("received message");
}

const name = 'Josh Perez';
const element = (
	<ul id="captchas">
		
	</ul>
);

function CaptchaContainer(props) {
	return (
		<ul id="captchas">
			{
				props.items.map((item, index) => (
					<li key={index}>
						<CaptchaItem image={item} />
					</li>
				))
			}
		</ul>
	);
}

class CaptchaItem extends React.Component {
	render() {
		return (
			<div>
				<img className="captcha-img" src={this.props.image} />
			</div>
		);
	}
}

ReactDOM.render(
    <CaptchaContainer items={captchas} />,
    document.getElementById('root')
);