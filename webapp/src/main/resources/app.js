class CaptchaApp extends React.Component {
	constructor(props) {
		super(props);
		this.removeCaptchaItem = this.removeCaptchaItem.bind(this);
		this.sendCaptcha = this.sendCaptcha.bind(this);

		this.state = { captchas: [] };
	}

	componentDidMount() {
		this.ws = new WebSocket("ws://10.57.31.112:5557/ws");
		this.ws.onmessage = e => {
			console.log("Received ws message!");

			const json = JSON.parse(e.data);
			const newCaptcha = [
				{
					"data": json.data,
					"publisherId": json.publisherId,
					"captchaId": json.captchaId
				}
			];

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
		console.log("captchas=" + JSON.stringify(this.state.captchas));

		this.setState((prevState, props) => {
			prevState.captchas.splice(index, 1);
			const captchas = prevState.captchas.slice();
			return { captchas: captchas }
		});
	}

	sendCaptcha(captcha) {
		console.log("Trying to send captcha...");

		this.ws.send(JSON.stringify(captcha));
	}

	render() {
		return (
			<CaptchaList items={this.state.captchas}
			             removeCaptchaItem={this.removeCaptchaItem}
			             sendCaptcha={this.sendCaptcha} />
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
										 removeCaptchaItem={this.props.removeCaptchaItem}
										 sendCaptcha={this.props.sendCaptcha} />
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
		const index = parseInt(this.props.index);
		this.props.removeCaptchaItem(index);
		
		const captcha = { data: "some data" };

		this.props.sendCaptcha(captcha);
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
						<p>Captcha id: {this.props.item.captchaId}</p>
						<p>Index: {this.props.index}</p>
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
