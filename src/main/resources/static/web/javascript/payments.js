const {createApp} = Vue

const payments = createApp({
    data(){
        return {
            cards:[],
            clientsAccount: {},
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            selectType: "ownAccount",
            selectOwner:"asdasd",

            targetsNumber: "",
            accountTarget:"",
            accountTargetOther: "",
            targetAssociated: "",
            targetAssociatedArray: [],

            description: "",

            targetsNumberArray: [],
            targetsArray: [],
            CardsAsociate: [],
            username: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundTitle: "",
            shadowCard: "",
            bgGrey: "",
            backgroundLogo: "",
            optionBackground: "",
            shadowCardLoan: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: "",

            view: true,

            viewPassword: "d-none",
            noViewPassword: "",
            cvv: 0,
            amount: 0,
            
            
            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("/api/clients/current")
            .then((data) =>{
                this.cards = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                
                this.targetsNumberArray = data.data.accountDTO.filter(account => account.active === true).map(account => account.number).sort((a,b) => a - b)
                this.targetsNumber = this.targetsNumberArray[0]
                this.targetsArray = data.data.accountDTO.filter(account => account.active === true)
                 
                console.log(this.targetsArray)
                this.accountOrigin = [... data.data.accountDTO.map(number => number.number)][0]
                
                console.log(this.cards)
                
            })
            .catch((error) => console.log(error))

            
        },
        collapseHeaderMovile(){
            if (this.header == "mob-menu-opened"){
                this.header = ""
            } else {
                this.header = "mob-menu-opened"
            }
        },
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        updateAmount(event) {
            this.amount = event.target.value;
        },
        swapValues() {
            const temp = this.accountOrigin;
            this.accountOrigin = this.accountTarget;
            this.accountTarget = temp;
        }, 
        payments() {

            Swal.fire({
                title: '¿Do you make pay?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {
                    if (this.amount > 0 && this.description != "" && this.targetAssociated != "") {

                        axios.post('/api/clients/current/transactions/payments', {number:this.targetAssociated,
                                                                                amount:this.amount,
                                                                                cvv:this.cvv,
                                                                                description:this.description,
                                                                                destiny:this.accountTargetOther
                                                                                },
                                                                                { headers: { 'content-type': 'application/json' } }
                                                                                )
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {

                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                })
                            })
                    } else {
                        Swal.fire('Transaction Failed', 'Complete all fields')
                                    .then(result => {
                                        window.location.reload()
                                    })

                    }
                    
                    
                    
                } else if (result.isDenied) {
                    Swal.fire('Cancel transaction', '', 'error')
                }
            })
    
        }
       
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundTitle = "bg-dark"
          this.shadowCard = "shadowWhite" 
          this.bgGrey = "bg-light borderRadius"
          this.backgroundLogo = ""
          this.optionBackground = "optionBackground-black"
          this.shadowCardLoan = "borderCard-white"
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundTitle = "backgroundTitletarget"
          this.shadowCard = "shadow  bg-body rounded" 
          this.bgGrey = "backgroundBodyCard borderRadius"
          this.backgroundLogo = ""
          this.optionBackground = "optionBackground-white"
          this.shadowCardLoan = "borderCard-black"
          localStorage.setItem("mode", false)
        }
    },
    targetAsociate(){
        const targetAssociatedFind = this.targetsArray.find(target => target.number === this.targetsNumber)
        if (targetAssociatedFind === undefined) {
            this.targetAssociated = 'Valor por defecto'
           
          } else {
            this.CardsAsociate = targetAssociatedFind.cards
            
            
          }
        
    },
    cardSelected() {
        const targetAssociatedFindCvv = this.cards.filter(target => target.account === this.targetsNumber).map(card => card.cvv)
        if (targetAssociatedFindCvv === undefined) {
            this.targetAssociated = 'Valor por defecto'
           
          } else {
            this.cvv = targetAssociatedFindCvv[0]
            
            
            
          }
        
    },
    
    

    
    
    
    }
})


payments.mount('#payments')
