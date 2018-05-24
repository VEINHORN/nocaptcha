class CaptchaApp extends React.Component {
	constructor(props) {
		super(props);
		this.removeCaptchaItem = this.removeCaptchaItem.bind(this);

		this.state = { captchas: [] };
	}

	componentDidMount() {
		this.ws = new WebSocket("ws://10.57.31.112:5557/ws");
		this.ws.onmessage = e => {
			console.log("Received ws message!");

			const json = JSON.parse(e.data);
			const newCaptcha = [ { "data": json.data, "publisherId": json.publisherId } ];

			this.setState((prevState, props) => ({
				captchas: prevState.captchas.concat(newCaptcha)
			}));
		}

		console.log("Here we should send initial request to the server!");
	}

	componentWillUnmount() {
		this.ws.close();
	}

	removeCaptchaItem(index) {
		console.log("removed captcha item");
	}

	render() {
		return (
			<CaptchaList items={this.state.captchas}
			             removeCaptchaItem={this.removeCaptchaItem} />
		)
	}
}

class CaptchaList extends React.Component {
	render() {
		return (
			<ul id="captchas">
				{
					this.props.items.map((item, index) => (
						<CaptchaListItem key={index}
						index={index}
						item={item}
						removeItem={this.props.removeCaptchaItem} />
					))
				}
			</ul>
		)
	}
}

class CaptchaListItem extends React.Component {
	constructor(props) {
		super(props);
		this.onSendClick = this.onSendClick.bind(this);
	}

	onSendClick() {
		console.log("Clicked on Send button!");
	}

	render() {
		return (
			<li>
				<div className="captcha-item">
					<div>
						<img className="captcha-img" src={this.props.item.data} />
					</div>

					<div className="captcha-control">
						<label htmlFor="recognized">Recognize captcha:</label>
						<input type="text" id="recognized" />
						<input type="submit" value="Send" onClick={this.onSendClick} />
						<p>Published by: {this.props.item.publisherId}</p>
					</div>
				</div>
			</li>
		)
	}
}

ReactDOM.render(
    <CaptchaApp />,
    document.getElementById('app')
);
